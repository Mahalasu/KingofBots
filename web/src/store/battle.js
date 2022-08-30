export default {
  state: {
    status: 'matching',
    socket: null,
    opponentUsername: 'opponent',
    opponentAvatar:
      'https://southernplasticsurgery.com.au/wp-content/uploads/2013/10/user-placeholder.png',
    gameMap: null,
    playerAId: 0,
    playerAsx: 0,
    playerAsy: 0,
    playerBId: 0,
    playerBsx: 0,
    playerBsy: 0,
    gameObject: null,
  },
  getters: {},
  mutations: {
    updateSocket(state, socket) {
      state.socket = socket
    },
    updateOpponent(state, opponent) {
      state.opponentUsername = opponent.username
      state.opponentAvatar = opponent.avatar
    },
    updateStatus(state, status) {
      state.status = status
    },
    updateGame(state, game) {
      state.gameMap = game.gameMap
      state.playerAId = game.playerAId
      state.playerBId = game.playerBId
      state.playerAsx = game.playerAsx
      state.playerAsy = game.playerAsy
      state.playerBsx = game.playerBsx
      state.playerBsy = game.playerBsy
    },
    updateGameObject(state, gameObject) {
      state.gameObject = gameObject
    },
  },
  actions: {},
  modules: {},
}
