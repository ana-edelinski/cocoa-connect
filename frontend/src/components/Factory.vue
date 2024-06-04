<template>
  <div>
    <h1>{{ msg }}</h1>
    <table>
      <thead>
        <tr>
          <th>Logo</th>
          <th>Name</th>
          <th>City</th>
          <th>Country</th>
          <th>Average Rating</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="factory in filtredFactories" :key="factory.id">
          <td><img :src="factory.image" alt="Logo" width="50" /></td>
          <td>{{ factory.name }}</td>
          <td>{{ factory.city }}</td>
          <td>{{ factory.country }}</td>
          <td>{{ factory.averageRating }}</td>
          <td><button type="submit" @click="viewChocolates(factory.id)"> View chocolates </button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

defineProps({
  msg: {
    type: String,
    required: true
  }
});

const factories = ref([]);
const filtredFactories = ref([]);

onMounted(() => {
  loadFactories();
});

function loadFactories() {
  axios.get('http://localhost:8080/backend/rest/factories/')
    .then(response => {
      factories.value = response.data.sort((a, b) => {
        if (a.factoryStatus === 'OPENED' && b.factoryStatus !== 'OPENED') return -1;
        if (a.factoryStatus !== 'OPENED' && b.factoryStatus === 'OPENED') return 1;
        return 0;
      });
      filtredFactories.value = factories.value;
    })
    .catch(error => console.error(error));
}

function viewChocolates(factoryId) {
  router.push({ name: 'chocolates', params: { factoryId } });
}


</script>

<style>
table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
  font-size: 1em;
  font-family: 'Arial', sans-serif;
  min-width: 400px;
}

thead tr {
  background-color: #009879;
  color: #ffffff;
  text-align: left;
  font-weight: bold;
}

th, td {
  padding: 12px 15px;
  border: 1px solid #ddd;
}

tbody tr {
  border-bottom: 1px solid #ddd;
}

tbody tr:nth-of-type(even) {
  background-color: #f3f3f3;
}

tbody tr:last-of-type {
  border-bottom: 2px solid #009879;
}

img {
  max-width: 100%;
  height: auto;
}
</style>
