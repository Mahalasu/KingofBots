import { createStore } from 'vuex'
import ModuleUser from './user'
import ModuleBattle from './battle'
import ModuleGameHistory from './gameHistory'

export default createStore({
  state: {},
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    user: ModuleUser,
    battle: ModuleBattle,
    gameHistory: ModuleGameHistory,
  },
})
