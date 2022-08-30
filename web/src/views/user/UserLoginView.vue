<template>
  <ContentField>
    <div class="row justify-content-center">
      <div class="col-5">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input
              v-model="username"
              type="text"
              class="form-control"
              id="username"
              placeholder="please input your username"
              autocomplete="on"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input
              v-model="password"
              type="password"
              class="form-control"
              id="password"
              placeholder="please input your password"
              autocomplete="on"
            />
          </div>
          <div class="errorMessage">{{ errorMessage }}</div>
          <button type="submit" class="btn btn-primary mb-3 w-100">
            Submit
          </button>
          <button
            type="button"
            class="btn btn-danger mb-3 w-100"
            @click="jumpToRegister"
          >
            Register
          </button>
        </form>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField'
import { useStore } from 'vuex'
import { onMounted, ref } from 'vue'
import router from '@/router/index'

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore()
    let username = ref('')
    let password = ref('')
    let errorMessage = ref('')

    onMounted(() => {
      document.getElementById('username').focus()
    })

    const login = () => {
      errorMessage.value = ''
      store.dispatch('login', {
        username: username.value,
        password: password.value,
        success() {
          store.dispatch('getInfo', {
            success() {
              router.push({ name: 'HomeIndex' })
            },
          })
        },
        error(resp) {
          errorMessage.value = resp.errorMessage
        },
      })
    }

    const jumpToRegister = () => {
      router.push({ name: 'RegisterIndex' })
    }

    return {
      username,
      password,
      errorMessage,
      login,
      jumpToRegister,
    }
  },
}
</script>

<style scoped>
.errorMessage {
  color: red;
}
</style>
