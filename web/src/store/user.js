import $ from 'jquery'
import { generateFromString } from 'generate-avatar'
import { domainNameProd } from '@/global'

export default {
  state: {
    id: '',
    username: '',
    avatar: '',
    token: '',
    marks: '',
    isLogin: false,
  },
  getters: {},
  mutations: {
    updateUser(state, user) {
      state.id = user.id
      state.username = user.username
      state.avatar = user.avatar
      state.marks = user.marks
      state.isLogin = user.isLogin
    },
    updateToken(state, token) {
      state.token = token
      state.isLogin = true
    },
    logout(state) {
      state.id = ''
      state.username = ''
      state.avatar = ''
      state.token = ''
      state.isLogin = false
    },
  },
  actions: {
    login(context, data) {
      $.ajax({
        url: `${domainNameProd}/api/user/token`,
        type: 'post',
        data: {
          username: data.username,
          password: data.password,
        },
        success(resp) {
          if (resp.errorMessage === 'success') {
            localStorage.setItem('jwtToken', resp.token)
            context.commit('updateToken', resp.token)
            data.success(resp)
          } else {
            data.error(resp)
          }
        },
        error(resp) {
          data.error(resp)
        },
      })
    },
    getInfo(context, data) {
      $.ajax({
        url: `${domainNameProd}/api/user/info`,
        type: 'get',
        headers: {
          Authorization: 'Bearer ' + context.state.token,
        },
        success(resp) {
          if (resp.errorMessage === 'success') {
            context.commit('updateUser', {
              ...resp,
              isLogin: true,
            })
            data.success(resp)
          } else {
            data.error(resp)
          }
        },
        error(resp) {
          data.error(resp)
        },
      })
    },
    logout(context) {
      localStorage.removeItem('jwtToken')
      context.commit('logout')
    },
    register(context, data) {
      $.ajax({
        url: `${domainNameProd}/api/user/register`,
        type: 'post',
        data: {
          username: data.username,
          password: data.password,
          confirmedPassword: data.confirmedPassword,
          avatarStr: generateFromString(data.username),
        },
        success(resp) {
          if (resp.errorMessage === 'success') {
            context.commit('updateToken', resp.token)
            data.success(resp)
          } else {
            data.error(resp)
          }
        },
        error(resp) {
          data.error(resp)
        },
      })
    },
  },
  modules: {},
}
