<template>
  <div>
    <h1>{{ msg }}</h1>
    <table>
      <thead>
        <tr>
          <th>Image</th>
          <th>Name</th>
          <th>Price</th>
          <th>Type</th>
          <th>Category</th>
          <th>Weight</th>
          <th>Description</th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="chocolate in chocolates" :key="chocolate.id">
          <td><img :src="chocolate.image" alt="Image" width="50" /></td>
          <td>{{ chocolate.name }}</td>
          <td>{{ chocolate.price }}</td>
          <td>{{ chocolate.type }}</td>
          <td>{{ chocolate.category }}</td>
          <td>{{ chocolate.weight }} g</td>
          <td>{{ chocolate.description }}</td>
          <td>
            <button type="submit" @click="deleteChocolate(chocolate.id)">Delete</button>
          </td>
          <td>
            <button type="submit" @click="editChocolate(chocolate.id)">Edit</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();
const factoryId = route.params.factoryId; 


defineProps({
  msg: {
    type: String,
    required: true
  }
});

const chocolates = ref([]);

onMounted(() => {
  loadChocolates();
});

function loadChocolates() {
  axios.get('http://localhost:8080/chocolate-factory/rest/chocolates/getAllForFactory/' + factoryId)
    .then(response => {
      console.log(chocolates.value);
      chocolates.value = response.data;
    })
    .catch(error => console.error(error));
}
function deleteChocolate(chocolateId) {
  axios.delete('http://localhost:8080/chocolate-factory/rest/chocolates/'+chocolateId)
    .then(() => {
      chocolates.value = chocolates.value.filter(chocolate => chocolate.id !== chocolateId);
    })
    .catch(error => console.error(error));
}


function editChocolate(chocolateId) {
  router.push({ name: 'editChocolate', params: { chocolateId } });
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
