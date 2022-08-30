import { GameObject } from '@/assets/scripts/GameObject'

export class Obstacle extends GameObject {
  constructor(row, col, gameMap) {
    super()

    this.row = row
    this.col = col
    this.gameMap = gameMap
    this.color = '#000000'
  }

  update() {
    this.render()
  }

  render() {
    const sideLength = this.gameMap.sideLength
    const ctx = this.gameMap.ctx
    ctx.fillStyle = this.color
    ctx.fillRect(
      this.col * sideLength,
      this.row * sideLength,
      sideLength,
      sideLength,
    )
  }
}
