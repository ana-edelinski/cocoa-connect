<template>
  <div class="cart-container">
    <h1>Your Shopping Cart</h1>
    <div class="cart-items" v-if="cart.length">
      <div class="cart-item" v-for="item in cart" :key="item.id">
        <img :src="item.chocolate.image" alt="Chocolate Image" class="item-image" />
        <div class="item-details">
          <h3>{{ item.chocolate.name }}</h3>
          <p><strong>Weight:</strong> {{ item.chocolate.weight }} g</p>
          <p><strong>Type:</strong> {{ item.chocolate.type }}</p>
          <p><strong>Kind:</strong> {{ item.chocolate.kind }}</p>
          <p><strong>Price:</strong> {{ item.chocolate.price }} RSD</p>
          <div class="quantity-controls">
            <button class="quantity-button" @click="decreaseQuantity(item)">-</button>
            <input v-model.number="item.quantity" type="number" @change="updateQuantity(item)" />
            <button class="quantity-button" @click="increaseQuantity(item)">+</button>
          </div>
          <button class="remove-button" @click="removeItem(item)">Remove</button>
        </div>
      </div>
      <div class="cart-total">
        <h2>Total Price: {{ totalPrice }} RSD</h2>
      </div>
    </div>
    <div v-else>
      <p>Your cart is empty.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Cart',
  data() {
    return {
      cart: []
    }
  },
  computed: {
    totalPrice() {
      return this.cart.reduce((total, item) => total + item.chocolate.price * item.quantity, 0);
    }
  },
  mounted() {
    let cartStr = localStorage.getItem('cart');
    this.cart = JSON.parse(cartStr);
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
    saveCart(){
        const cartForSave = JSON.stringify(this.cart);
        localStorage.setItem('cart', cartForSave);
    },
    removeItem(item) {
      this.cart = this.cart.filter(cartItem => cartItem.chocolate.id !== item.chocolate.id);
      this.saveCart();
    }
  }
}
</script>

<style>
.cart-container {
  width: 90%;
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  font-family: 'Poppins', sans-serif;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #2c3e50;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.cart-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.item-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 10px;
}

.item-details {
  flex: 1;
}

.item-details h3 {
  margin: 0 0 10px;
  color: #333;
}

.item-details p {
  margin: 5px 0;
  color: #666;
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
  background-color: #2c3e50;
  color: #fff;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  cursor: pointer;
}

.quantity-button:hover {
  background-color: #1a252f;
}

.remove-button {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.remove-button:hover {
  background-color: #c0392b;
}

.cart-total {
  text-align: right;
  font-size: 1.5em;
  margin-top: 20px;
  color: #2c3e50;
}
</style>
