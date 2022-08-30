<template>
  <GameMap :color="color" v-if="$store.state.battle.status === 'playing'" />
  <MatchPage v-if="$store.state.battle.status === 'matching'" />
  <div
    class="modal fade"
    tabindex="-1"
    id="resultModal"
    data-bs-backdrop="static"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Game Result</h5>
          <button
            @click="restart"
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          {{ result }}
        </div>
        <div class="modal-footer">
          <button
            @click="restart"
            type="button"
            class="btn btn-primary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GameMap from '@/components/GameMap'
import MatchPage from '@/components/MatchPage'
import { onMounted, onUnmounted, ref } from 'vue'
import { useStore } from 'vuex'
import bootstrap from 'bootstrap/dist/js/bootstrap'
import { websocketNameProd } from '@/global'

export default {
  components: {
    GameMap,
    MatchPage,
  },
  setup() {
    const store = useStore()
    store.commit('updateIsRecord', false)
    let socket = null
    const socketUrl = `${websocketNameProd}/websocket/${store.state.user.token}`
    const result = ref('')
    const color = ref('')
    let resultModal

    onMounted(() => {
      resultModal = new bootstrap.Modal('#resultModal', {
        keyboard: false,
      })

      socket = new WebSocket(socketUrl)

      socket.onopen = () => {
        console.log('connected')
        store.commit('updateSocket', socket)
      }

      socket.onmessage = (msg) => {
        const data = JSON.parse(msg.data)
        if (data.event === 'opponentFound') {
          console.log('matching success')
          store.commit('updateOpponent', {
            username: data.opponentUsername,
            avatar: data.opponentAvatar,
          })
          setTimeout(() => {
            store.commit('updateStatus', 'playing')
          }, 100)
          store.commit('updateGame', data.game)
          color.value =
            store.state.battle.playerAId === parseInt(store.state.user.id)
              ? 'blue'
              : 'red'
        } else if (data.event === 'steps') {
          const game = store.state.battle.gameObject
          const [snake0, snake1] = game.snakes
          snake0.setDirection(data.aSteps)
          snake1.setDirection(data.bSteps)
        } else if (data.event === 'result') {
          const game = store.state.battle.gameObject
          const [snake0, snake1] = game.snakes
          if (data.loser === 'both' || data.loser === 'a') snake0.status = 'die'
          if (data.loser === 'both' || data.loser === 'b') snake1.status = 'die'

          if (data.loser === 'both') result.value = 'Draw!'
          else if (
            data.loser === 'a' &&
            store.state.battle.playerAId === parseInt(store.state.user.id)
          )
            result.value = 'You Lose!'
          else if (
            data.loser === 'b' &&
            store.state.battle.playerBId === parseInt(store.state.user.id)
          )
            result.value = 'You Lose!'
          else result.value = 'You Win!'
          resultModal.show()
        }
      }

      socket.onclose = () => {
        console.log('disconnected')
      }
    })

    onUnmounted(() => {
      socket.close()
      store.commit('updateStatus', 'matching')
    })

    const restart = () => {
      resultModal.hide()
      store.commit('updateStatus', 'matching')
      store.commit('updateOpponent', {
        username: 'opponent',
        avatar:
          'https://southernplasticsurgery.com.au/wp-content/uploads/2013/10/user-placeholder.png',
      })
    }

    return {
      result,
      color,
      restart,
    }
  },
}
</script>

<style scoped></style>
