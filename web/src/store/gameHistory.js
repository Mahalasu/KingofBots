export default {
  state: {
    isRecord: false,
    aSteps: '',
    bSteps: '',
    recordLoser: '',
  },
  getters: {},
  mutations: {
    updateIsRecord(state, isRecord) {
      state.isRecord = isRecord
    },
    updateSteps(state, data) {
      state.aSteps = data.aSteps
      state.bSteps = data.bSteps
    },
    updateRecordLoser(state, loser) {
      state.recordLoser = loser
    },
  },
  actions: {},
  modules: {},
}
