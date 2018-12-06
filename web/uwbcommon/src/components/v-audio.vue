<template>
  <div class="audio">
    <audio volume="1.0" ref="voice">
      <source src="../assets/audio/smzy_2016062205.mp3" >
    </audio>
  </div>
</template>
<script>
export default {
  name: 'v-audio',
  props: {
    num: {type: Number}
  },
  methods: {
    play () {
      this.$refs.voice.play()
    }
  },
  watch: {
    num (val) {
      if (val !== 0) {
        this.play()
      }
    }
  }
}
</script>
