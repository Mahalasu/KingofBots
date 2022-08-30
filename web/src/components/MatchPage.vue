<template>
  <div class="container matchPage">
    <div class="row">
      <div class="col-6 text-center">
        <img
          class="rounded-circle avatar"
          :src="'data:image/svg+xml;utf8,' + $store.state.user.avatar"
          alt=""
        />
        <div class="fs-3 fw-bold py-3">
          {{ $store.state.user.username }}
        </div>
      </div>
      <div class="col-6 text-center">
        <img
          class="rounded-circle avatar"
          :src="
            $store.state.battle.opponentAvatar.startsWith('https')
              ? $store.state.battle.opponentAvatar
              : 'data:image/svg+xml;utf8,' + $store.state.battle.opponentAvatar
          "
          alt=""
        />
        <div class="fs-3 fw-bold py-3">
          {{ $store.state.battle.opponentUsername }}
        </div>
      </div>
      <select
        v-model="selectBotId"
        class="form-select w-50 mx-auto"
        aria-label="selectMethod"
      >
        <option selected value="-1">Play By Yourself</option>
        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
          {{ bot.name }}
        </option>
      </select>
      <div class="col-12 text-center mt-5">
        <button type="button" class="btn btn-danger btn-lg" @click="onClick">
          {{ btnText }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex'
import $ from 'jquery'
import { domainNameProd } from '@/global'

export default {
  setup() {
    const store = useStore()
    let btnText = ref('Start Matching')
    let bots = ref([])
    let selectBotId = ref('-1')

    const getList = () => {
      $.ajax({
        url: `${domainNameProd}/api/user/bot/list`,
        type: 'get',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          bots.value = resp
        },
      })
    }

    getList()

    const onClick = () => {
      if (btnText.value === 'Start Matching') {
        btnText.value = 'Cancel'
        store.state.battle.socket.send(
          JSON.stringify({
            event: 'startMatching',
            botId: selectBotId.value,
          }),
        )
      } else {
        btnText.value = 'Start Matching'
        store.state.battle.socket.send(
          JSON.stringify({
            event: 'cancelMatching',
          }),
        )
      }
    }

    return {
      onClick,
      btnText,
      bots,
      selectBotId,
    }
  },
}
</script>

<style scoped>
div.matchPage {
  width: 60vw;
  height: 70vh;
  margin: 50px auto;
}

.avatar {
  height: 230px;
}

.btn {
  width: 300px;
}
</style>
