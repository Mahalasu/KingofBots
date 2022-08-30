import { GameObject } from '@/assets/scripts/GameObject'
import { Cell } from '@/assets/scripts/Cell'

export class Snake extends GameObject {
  constructor(info, gameMap) {
    super()

    this.id = info.id
    this.color = info.color
    this.gameMap = gameMap

    // store the body of the snakes
    this.cells = [new Cell(info.row, info.col)]
    // the cell which the snake goes to
    this.nextCell = null

    // 5 cells per second
    this.speed = 5
    // -1: no instructs, 0: up, 1: right, 2: down, 3: left
    this.direction = -1
    // idle: no action, move: move action, die: snake died
    this.status = 'idle'

    // offset on each direction
    this.dr = [-1, 0, 1, 0]
    this.dc = [0, 1, 0, -1]

    // rounds of the game
    this.round = 0
    // acceptable deviation
    this.eps = 1e-2

    // snakes' eyes info
    this.eyeDirection = this.id === 0 ? 0 : 2
    this.eyeDx = [
      [-1, 1],
      [1, 1],
      [1, -1],
      [-1, -1],
    ]
    this.eyeDy = [
      [-1, -1],
      [-1, 1],
      [1, 1],
      [1, -1],
    ]
  }

  start() {}

  setDirection(d) {
    this.direction = d
  }

  // whether the tail will increase
  checkTailInc() {
    return this.round <= 10 || this.round % 3 === 1
  }

  nextStep() {
    const d = this.direction
    this.nextCell = new Cell(
      this.cells[0].row + this.dr[d],
      this.cells[0].col + this.dc[d],
    )
    this.direction = -1
    this.eyeDirection = d
    this.status = 'move'
    this.round++

    const k = this.cells.length
    for (let i = k; i > 0; i--)
      this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]))
  }

  updateMove() {
    const dx = this.nextCell.x - this.cells[0].x
    const dy = this.nextCell.y - this.cells[0].y
    const distance = Math.sqrt(dx * dx + dy * dy)

    if (distance < this.eps) {
      this.cells[0] = this.nextCell
      this.nextCell = null
      this.status = 'idle'

      if (!this.checkTailInc()) this.cells.pop()
    } else {
      const moveDistance = (this.speed * this.timeDelta) / 1000
      this.cells[0].x += (moveDistance * dx) / distance
      this.cells[0].y += (moveDistance * dy) / distance

      if (!this.checkTailInc()) {
        const k = this.cells.length
        const tail = this.cells[k - 1],
          tailTarget = this.cells[k - 2]
        const tailDx = tailTarget.x - tail.x
        const tailDy = tailTarget.y - tail.y
        tail.x += (moveDistance * tailDx) / distance
        tail.y += (moveDistance * tailDy) / distance
      }
    }
  }

  update() {
    if (this.status === 'move') this.updateMove()
    this.render()
  }

  render() {
    const sideLength = this.gameMap.sideLength
    const ctx = this.gameMap.ctx

    ctx.fillStyle = this.color
    if (this.status === 'die') ctx.fillStyle = '#808080'

    for (const cell of this.cells) {
      ctx.beginPath()
      ctx.arc(
        cell.x * sideLength,
        cell.y * sideLength,
        (sideLength / 2) * 0.7,
        0,
        Math.PI * 2,
      )
      ctx.fill()
    }

    for (let i = 1; i < this.cells.length; i++) {
      const c1 = this.cells[i - 1],
        c2 = this.cells[i]
      if (Math.abs(c1.x - c2.x) < this.eps && Math.abs(c1.y - c2.y) < this.eps)
        continue
      if (Math.abs(c1.x - c2.x) < this.eps) {
        ctx.fillRect(
          (c1.x - 0.35) * sideLength,
          Math.min(c1.y, c2.y) * sideLength,
          sideLength * 0.7,
          Math.abs(c1.y - c2.y) * sideLength,
        )
      } else {
        ctx.fillRect(
          Math.min(c1.x, c2.x) * sideLength,
          (c1.y - 0.35) * sideLength,
          Math.abs(c1.x - c2.x) * sideLength,
          sideLength * 0.7,
        )
      }
    }

    ctx.fillStyle = 'black'
    for (let i = 0; i < 2; i++) {
      const eyeX =
        (this.cells[0].x + this.eyeDx[this.eyeDirection][i] * 0.15) * sideLength
      const eyeY =
        (this.cells[0].y + this.eyeDy[this.eyeDirection][i] * 0.15) * sideLength

      ctx.beginPath()
      ctx.arc(eyeX, eyeY, sideLength * 0.05, 0, Math.PI * 2)
      ctx.fill()
    }
  }
}
