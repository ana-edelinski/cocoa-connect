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
    <div class="feedback-section" v-if="order.status === 'APPROVED'">
      <h3>Leave Your Feedback</h3>
      <div class="rating">
        <label>Rating:</label>
        <div class="rating-options">
          <label v-for="n in 5" :key="n">
            <input type="radio" v-model="rating" :value="n"> {{ n }}
          </label>
        </div>
      </div>
      <div class="comment">
        <label for="comment">Comment:</label>
        <textarea v-model="comment" rows="3"></textarea>
      </div>
      <button @click="submitFeedback">Submit</button>
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
const rating = ref(null);
const comment = ref('');
const order = ref({});

onMounted(() => {
  loadOrderItems();
});

function loadOrderItems() {
  axios.get(`http://localhost:8080/chocolate-factory/rest/items/forOrder/${orderId}`)
    .then(response => {
      orderItems.value = response.data;
    })
    .catch(error => console.error('err ' + error));
  axios.get(`http://localhost:8080/chocolate-factory/rest/orders/${orderId}`)
    .then(response => {
      order.value = response.data;
    })
    .catch(error => console.error('err ' + error));
}

function submitFeedback() {
  const feedback = {
    orderId: orderId,
    rating: rating.value,
    comment: comment.value
  };
  
  axios.post('http://localhost:8080/chocolate-factory/rest/comments', feedback)
    .then(response => {
      console.log('Feedback submitted', response.data);
      rating.value = null;
      comment.value = '';
    })
    .catch(error => alert('You have already commented this order: '));
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

.feedback-section {
  margin-top: 30px;
  text-align: center;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  padding: 20px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.feedback-section h3 {
  margin-bottom: 20px;
  font-size: 24px;
}

.rating, .comment {
  margin-top: 10px;
  text-align: left;
}

.rating label, .comment label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.rating-options {
  display: flex;
  gap: 10px;
  align-items: center;
}

.rating-options label {
  display: flex;
  align-items: center;
}

.rating-options input[type="radio"] {
  margin-right: 5px;
}

.comment textarea {
  width: calc(100% - 20px);
  padding: 10px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-sizing: border-box;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #523F31;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}

button:hover {
  background-color: #735B4F;
}
</style>
