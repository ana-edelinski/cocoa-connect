<template>
  <div class="order-info">
    <h2>Order Information</h2>
    <div class="chocolates">
      <div class="chocolate-card" v-for="item in orderItems" :key="item.chocolate.id">
        <div class="price-ribbon">{{ item.chocolate.price }} RSD</div>
        <img :src="item.chocolate.image" alt="Chocolate Image" class="chocolate-image">
        <div class="chocolate-details">
          <h3>{{ item.chocolate.name }}</h3>
          <p class="chocolate-detail"><strong>Type:</strong> {{ item.chocolate.type }}</p>
          <p class="chocolate-detail"><strong>Kind:</strong> {{ item.chocolate.kind }}</p>
          <p class="chocolate-detail"><strong>Weight:</strong> {{ item.chocolate.weight }} g</p>
          <p class="chocolate-detail"><strong>Quantity:</strong> {{ item.quantity }}</p>
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
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: "Poppins", sans-serif;
}

.chocolates {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.chocolate-card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  width: 350px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
  transition: transform 0.2s;
}

.chocolate-card:hover {
  transform: scale(1.05);
}

.chocolate-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  object-position: center;
  border-radius: 5px;
  margin-bottom: 10px;
}

.chocolate-details {
  text-align: center;
}

.chocolate-details h3 {
  margin: 10px 0 5px;
}

.chocolate-price {
  color: #523F31;
  margin: 0 0 5px;
  font-size: 18px;
}

.chocolate-detail {
  margin: 5px 0;
}

.price-ribbon {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #523F31;
  color: #fff;
  padding: 5px 10px;
  font-size: 14px;
  font-weight: bold;
  border-radius: 5px;
  z-index: 1;
}

</style>
