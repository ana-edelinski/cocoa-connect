<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <p>
      Welcome to chocolate site
     
    </p>
    
  </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted } from 'vue';

defineProps({
  msg: {
    type: String,
    required: true
  }
})

onMounted(() => {
})

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #43392F;
}
</style>
