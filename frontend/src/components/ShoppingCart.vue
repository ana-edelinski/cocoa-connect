<template>
  <div class="cart-container">
    <h1>Shopping Cart</h1>
    <div class="cart-items" v-if="cart.items.length">
      <div class="cart-item" v-for="item in cart.items" :key="item.chocolate.id">
        <img :src="item.chocolate.image" alt="Chocolate Image" class="item-image" />
        <div class="item-details">
          <h3>{{ item.chocolate.name }}</h3>
          <p class="chocolate-price"><strong> {{ item.chocolate.price }} RSD </strong></p>
          <p><strong>Type:</strong> {{ item.chocolate.type }}</p>
          <p><strong>Kind:</strong> {{ item.chocolate.kind }}</p>
          <p><strong>Weight:</strong> {{ item.chocolate.weight }} g</p>
          <div class="quantity-controls">
            <button class="quantity-button" @click="decreaseQuantity(item)">-</button>
            <input v-model.number="item.quantity" type="number" @change="updateQuantity(item)" />
            <button class="quantity-button" @click="increaseQuantity(item)">+</button>
          </div>
          <button class="remove-button" @click="removeItem(item)">🗑</button>
        </div>
      </div>
      <div class="cart-total">
        <h2>Total Price: {{ totalPrice }} RSD</h2>
        <button class="buy-now-button" @click="buyNow">Buy Now</button>
      </div>
    </div>
    <div v-else>
      <p>Your cart is empty.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Cart',
  data() {
    return {
      cart: {
        "items" : [],
        factoryId: null
      }
    }
  },
  computed: {
    totalPrice() {
      return this.cart.items.reduce((total, item) => total + item.chocolate.price * item.quantity, 0);
    }
  },
  mounted() {
    let cartStr = localStorage.getItem('cart');
    if(cartStr){
      this.cart = JSON.parse(cartStr);
    }
  },
  methods: {
    increaseQuantity(item) {
      if (item.quantity < item.chocolate.quantity) {
        item.quantity++;
        this.saveCart();
      }
    },
    decreaseQuantity(item) {
      if (item.quantity > 1) {
        item.quantity--;
        this.saveCart();
      }
    },
    updateQuantity(item) {
      if (item.quantity < 1) {
        item.quantity = 1;
      } else if (item.quantity > item.chocolate.quantity) {
        item.quantity = item.chocolate.quantity;
      }
    },
    saveCart() {
      const cartForSave = JSON.stringify(this.cart);
      localStorage.setItem('cart', cartForSave);
    },
    removeItem(item) {
      this.cart.items = this.cart.items.filter(cartItem => cartItem.chocolate.id !== item.chocolate.id);
      this.saveCart();
    },
    buyNow() {
      const storedUser = localStorage.getItem('loggedUser');  
      const userId = JSON.parse(storedUser).id;

      const cartDto = {"items" : this.cart.items, "loggedId": userId, "factoryId" : this.cart.factoryId};

      axios.post('http://localhost:8080/chocolate-factory/rest/orders/', cartDto )
        .then(response => {
          localStorage.removeItem('cart');
          this.cart = {"items" : []};
          alert('Succesful order');
        })
        .catch(error => {
          alert('error ordering');

          console.error('Error fetching factories:', error);
        });
    }
  }
}
</script>

<style>
.cart-container {
  overflow: hidden;
  width: 90%;
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #2D1E17;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 15px;
}

.cart-item {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
  justify-content: space-between;
}

.cart-item:hover {
  transform: scale(1.05);
}

.item-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 10px;
  margin-left: 40px;
}

.item-details {
  flex: 1;
}

.item-details h3 {
  margin: 0 0 10px;
  color: #2d1e17;
}

.item-details p {
  margin: 5px 0;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.quantity-controls input {
  width: 50px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 5px;
}

.quantity-button {
  background-color: white;
  color: #523F31;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  cursor: pointer;
}

.quantity-button:hover {
  background-color: #523F31;
  color: #fff
}

.remove-button {
  background-color: #523F31;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.remove-button:hover {
  background-color: #796254;
}

.cart-total {
  text-align: right;
  font-size: 1.2em;
  margin-top: 20px;
  color: #2d1e17;
}

.chocolate-price {
  color: #523F31;
  margin: 0 0 5px;
  font-size: 18px;
}

.buy-now-button {
  background-color: #523F31;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.buy-now-button:hover {
  background-color: #796254;
}
</style>
