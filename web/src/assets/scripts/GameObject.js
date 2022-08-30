const gameObjects = []

export class GameObject {
  constructor() {
    gameObjects.push(this)
    this.timeDelta = 0
    this.hasCalledStart = false
  }

  // execute only once
  start() {}

  // execute every frame, except for the first frame
  update() {}

  // execute before deleting the object
  onDestroy() {}

  destroy() {
    this.onDestroy()
    for (let idx = 0; idx < gameObjects.length; idx++) {
      const obj = gameObjects[idx]
      if (obj === this) {
        gameObjects.splice(idx)
        break
      }
    }
  }
}

let lastTimeStamp
const step = (timeStamp) => {
  for (let obj of gameObjects) {
    if (!obj.hasCalledStart) {
      obj.hasCalledStart = true
      obj.start()
    } else {
      obj.timeDelta = timeStamp - lastTimeStamp
      obj.update()
    }
  }
  lastTimeStamp = timeStamp
  requestAnimationFrame(step)
}

requestAnimationFrame(step)
