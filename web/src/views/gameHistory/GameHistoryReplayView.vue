<template>
  <GameMap :color="color" />
</template>

<script>
import GameMap from '@/components/GameMap'
import { useStore } from 'vuex'
import { ref } from 'vue'

export default {
  components: {
    GameMap,
  },
  setup() {
    const store = useStore()
    const color = ref('')
    color.value =
      store.state.battle.playerAId === parseInt(store.state.user.id)
        ? 'blue'
        : 'red'

    return {
      color,
    }
  },
}
</script>

<style scoped></style>
