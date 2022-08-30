<template>
  <div class="playground">
    <div class="gameMap" ref="parent">
      <canvas ref="canvas" tabindex="0"></canvas>
    </div>
    <div
      class="alert alert-warning d-flex align-items-center my-3 mx-auto"
      role="alert"
    >
      <div>
        <span class="fw-bold">Warning:</span> You are the
        <span id="color" class="fw-bold">{{ colorGot }}</span> snake!
      </div>
    </div>
  </div>
</template>

<script>
import { GameMap } from '@/assets/scripts/GameMap'
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'

export default {
  props: {
    color: String,
  },
  setup(props) {
    const colorGot = computed(() => props.color)
    const store = useStore()
    let parent = ref(null)
    let canvas = ref(null)

    onMounted(() =>
      store.commit(
        'updateGameObject',
        new GameMap(canvas.value.getContext('2d'), parent.value, store),
      ),
    )

    return {
      parent,
      canvas,
      colorGot,
    }
  },
}
</script>

<style scoped>
div.playground {
  width: 60vw;
  height: 70vh;
  margin: 50px auto;
}

div.gameMap {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.alert {
  height: 5vh;
  width: 50vw;
}

#color {
  color: v-bind(colorGot);
}
</style>
