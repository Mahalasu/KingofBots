<template>
  <nav class="navbar navbar-dark navbar-expand-lg bg-primary sticky-top">
    <div class="container">
      <router-link class="navbar-brand" :to="{ name: 'HomeIndex' }"
        >King of Bots</router-link
      >
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :class="
                routeName === 'BattleIndex' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'BattleIndex' }"
              >Start
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              :class="
                routeName === 'GameHistoryIndex'
                  ? 'nav-link active'
                  : 'nav-link'
              "
              :to="{ name: 'GameHistoryIndex' }"
              >Game History
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              :class="
                routeName === 'RankIndex' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'RankIndex' }"
            >
              Rank
            </router-link>
          </li>
          <li class="nav-item">
            <a
              class="nav-link"
              target="_blank"
              rel="noopener noreferrer"
              href="https://github.com/Mahalasu"
              >GitHub
              <span>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="14"
                  height="14"
                  fill="currentColor"
                  class="bi bi-box-arrow-up-right"
                  viewBox="0 0 17 17"
                >
                  <path
                    fill-rule="evenodd"
                    d="M8.636 3.5a.5.5 0 0 0-.5-.5H1.5A1.5 1.5 0 0 0 0 4.5v10A1.5 1.5 0 0 0 1.5 16h10a1.5 1.5 0 0 0 1.5-1.5V7.864a.5.5 0 0 0-1 0V14.5a.5.5 0 0 1-.5.5h-10a.5.5 0 0 1-.5-.5v-10a.5.5 0 0 1 .5-.5h6.636a.5.5 0 0 0 .5-.5z"
                  />
                  <path
                    fill-rule="evenodd"
                    d="M16 .5a.5.5 0 0 0-.5-.5h-5a.5.5 0 0 0 0 1h3.793L6.146 9.146a.5.5 0 1 0 .708.708L15 1.707V5.5a.5.5 0 0 0 1 0v-5z"
                  />
                </svg>
              </span>
            </a>
          </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.isLogin">
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href=""
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
              id="dropdownButton"
            >
              {{ $store.state.user.username }}
            </a>
            <ul class="dropdown-menu" aria-labelledby="dropdownButton">
              <li @click="updateInfo">
                <router-link class="dropdown-item" :to="{ name: 'BotIndex' }"
                  >My Bots</router-link
                >
              </li>
              <li>
                <hr class="dropdown-divider" />
              </li>
              <li>
                <a class="dropdown-item" @click="logout">Sign Out</a>
              </li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav" v-else>
          <li class="nav-item">
            <router-link
              :class="
                routeName === 'LoginIndex' ? 'nav-link active' : 'nav-link'
              "
              role="button"
              :to="{ name: 'LoginIndex' }"
            >
              Login
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              :class="
                routeName === 'RegisterIndex' ? 'nav-link active' : 'nav-link'
              "
              role="button"
              :to="{ name: 'RegisterIndex' }"
            >
              Register
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useStore } from 'vuex'
import router from '@/router/index'

export default {
  setup() {
    const store = useStore()
    const route = useRoute()
    let routeName = computed(() => route.name)

    const updateInfo = () => {
      store.dispatch('getInfo', {
        success() {},
      })
    }

    const logout = () => {
      store.dispatch('logout')
      router.push({ name: 'HomeIndex' })
    }

    return {
      routeName,
      logout,
      updateInfo,
    }
  },
}
</script>

<style scoped></style>
