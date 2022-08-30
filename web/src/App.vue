<template>
  <NavBar />
  <router-view />
</template>

<script>
import NavBar from '@/components/NavBar'
import store from '@/store/index'
import router from '@/router/index'

export default {
  components: {
    NavBar,
  },
  setup() {
    const jwtToken = localStorage.getItem('jwtToken')
    if (jwtToken) {
      store.commit('updateToken', jwtToken)
      store.dispatch('getInfo', {
        success() {},
        error() {
          router.push({ name: 'LoginIndex' })
        },
      })
    }
  },
}
</script>

<style scoped></style>
