import { GameObject } from '@/assets/scripts/GameObject'
import { Obstacle } from '@/assets/scripts/Obstacle'
import { Snake } from '@/assets/scripts/Snake'

export class GameMap extends GameObject {
  constructor(ctx, parent, store) {
    super()

    this.ctx = ctx
    this.parent = parent
    this.store = store
    this.sideLength = 0

    this.rows = 13
    this.cols = 14
    this.numOfObsts = 30
    this.obstacles = []

    this.snakes = [
      new Snake({ id: 0, color: '#53B7DB', row: this.rows - 1, col: 0 }, this),
      new Snake({ id: 1, color: '#EC6959', row: 0, col: this.cols - 1 }, this),
    ]
  }

  createObstacles() {
    const map = this.store.state.battle.gameMap

    for (let r = 0; r < this.rows; r++)
      for (let c = 0; c < this.cols; c++)
        if (map[r][c]) this.obstacles.push(new Obstacle(r, c, this))
  }

  addMoveListener() {
    if (this.store.state.gameHistory.isRecord) {
      let i = 0
      const aSteps = this.store.state.gameHistory.aSteps
      const bSteps = this.store.state.gameHistory.bSteps
      const loser = this.store.state.gameHistory.recordLoser
      const [snake0, snake1] = this.snakes
      const interval = setInterval(() => {
        if (i >= aSteps.length - 1) {
          if (loser === 'both' || loser === 'a') snake0.status = 'die'
          if (loser === 'both' || loser === 'b') snake1.status = 'die'
          clearInterval(interval)
        } else {
          snake0.setDirection(parseInt(aSteps[i]))
          snake1.setDirection(parseInt(bSteps[i]))
        }
        i++
      }, 300)
    } else {
      this.ctx.canvas.focus()
      this.ctx.canvas.addEventListener('keydown', (e) => {
        let d = -1
        if (e.key === 'w') d = 0
        else if (e.key === 'd') d = 1
        else if (e.key === 's') d = 2
        else if (e.key === 'a') d = 3

        if (d >= 0) {
          this.store.state.battle.socket.send(
            JSON.stringify({
              event: 'keyInputMove',
              direction: d,
            }),
          )
        }
      })
    }
  }

  start() {
    this.createObstacles()
    this.addMoveListener()
  }

  updateSize() {
    this.sideLength = Math.floor(
      Math.min(
        this.parent.clientWidth / this.cols,
        this.parent.clientHeight / this.rows,
      ),
    )
    this.ctx.canvas.width = this.sideLength * this.cols
    this.ctx.canvas.height = this.sideLength * this.rows
  }

  checkReady() {
    for (const snake of this.snakes) {
      if (snake.status !== 'idle') return false
      if (snake.direction === -1) return false
    }

    return true
  }

  nextStep() {
    for (const snake of this.snakes) {
      snake.nextStep()
    }
  }

  update() {
    this.updateSize()
    if (this.checkReady()) this.nextStep()
    this.render()
  }

  render() {
    this.ctx.strokeStyle = 'black'
    this.ctx.lineWidth = 6
    this.ctx.strokeRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height)

    this.ctx.lineWidth = 1.5
    for (let c = 1; c < this.cols; c++) {
      this.ctx.beginPath()
      this.ctx.moveTo(this.sideLength * c, 0)
      this.ctx.lineTo(this.sideLength * c, this.ctx.canvas.height)
      this.ctx.stroke()
    }

    for (let r = 1; r < this.rows; r++) {
      this.ctx.beginPath()
      this.ctx.moveTo(0, this.sideLength * r)
      this.ctx.lineTo(this.ctx.canvas.width, this.sideLength * r)
      this.ctx.stroke()
    }
  }
}
