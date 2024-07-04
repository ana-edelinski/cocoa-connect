<template>
  <div class="order-info">
    <h2>Order Information</h2>
    <div class="chocolates">
      <div class="chocolate" v-for="item in orderItems" :key="item.chocolate.id">
        <img :src="item.chocolate.image" alt="Chocolate Image" class="chocolate-image">
        <div class="chocolate-details">
          <h3>{{ item.chocolate.name }}</h3>
          <p><strong>Price:</strong> {{ item.chocolate.price }} RSD</p>
          <p><strong>Type:</strong> {{ item.chocolate.type }}</p>
          <p><strong>Kind:</strong> {{ item.chocolate.kind }}</p>
          <p><strong>Weight:</strong> {{ item.chocolate.weight }} g</p>
          <p><strong>Quantity:</strong> {{ item.quantity }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

const route = useRoute();
const orderId = route.params.orderId;
const orderItems = ref([]);

onMounted(() => {
    loadOrderItems();
});
function loadOrderItems() {
  axios.get(`http://localhost:8080/chocolate-factory/rest/items/forOrder/${orderId}`)
    .then(response => {
      orderItems.value = response.data;
    })
    .catch(error => console.error('err ' + error));
}

</script>

<style>
.order-info {
  width: 800px;
  margin: 50px auto;
}

.chocolates {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.chocolate {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 15px;
  width: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.chocolate-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-bottom: 10px;
}

.chocolate-details {
  text-align: center;
}
</style>
