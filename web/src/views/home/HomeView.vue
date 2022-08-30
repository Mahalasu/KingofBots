<template>
  <ContentField class="text-center">
    <img class="w-25 my-5" src="../../assets/images/logo.png" />
    <h1>Create The Best Snake Bot</h1>
    <h4 class="mt-3">🐍 use your bot to defeat others in the Snake game</h4>
    <button class="btn btn-danger mt-5" @click="start">Get Started</button>
    <hr />
    <p class="text-start fs-5">
      KingofBots(KOB) is a game battle platform. Here you can play Snake games
      with other players in person. You can also use your own designed AI to
      play against. Please login or register before play the game. Also, please
      follow the
      <a
        target="_blank"
        rel="noopener noreferrer"
        href="https://github.com/Mahalasu"
        >instructions</a
      >
      when you are creating your own bot.
    </p>
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField'
import router from '@/router/index'

export default {
  components: {
    ContentField,
  },
  setup() {
    const start = () => {
      router.push({ name: 'BattleIndex' })
    }

    return {
      start,
    }
  },
}
</script>

<style scoped></style>
