<template>
  <ContentField>
    <div class="row justify-content-center">
      <div class="col-5">
        <form @submit.prevent="register">
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
          <div class="mb-3">
            <label for="confirmed-password" class="form-label"
              >Confirm Password</label
            >
            <input
              v-model="confirmedPassword"
              type="password"
              class="form-control"
              id="confirmed-password"
              placeholder="please input your password again"
              autocomplete="on"
            />
          </div>
          <div class="errorMessage">{{ errorMessage }}</div>
          <button type="submit" class="btn btn-danger w-100">Register</button>
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
    let confirmedPassword = ref('')
    let errorMessage = ref('')

    onMounted(() => {
      document.getElementById('username').focus()
    })

    const register = () => {
      errorMessage.value = ''
      store.dispatch('register', {
        username: username.value,
        password: password.value,
        confirmedPassword: confirmedPassword.value,
        success() {
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
          })
        },
        error(resp) {
          errorMessage.value = resp.errorMessage
        },
      })
    }

    return {
      username,
      password,
      confirmedPassword,
      errorMessage,
      register,
    }
  },
}
</script>

<style scoped>
.errorMessage {
  color: red;
}
</style>
