<template>
  <div class="my-account">
    <div class="tabs">
      <button :class="{ active: currentTab === 'profile' }" @click="currentTab = 'profile'">MY PROFILE</button>
      <button v-if="loggedUser && loggedUser.role === 'CUSTOMER'" :class="{ active: currentTab === 'orders' }" @click="currentTab = 'orders'">MY ORDERS</button>
      <button v-if="loggedUser && loggedUser.role === 'MANAGER'" :class="{ active: currentTab === 'cancelRequests' }" @click="currentTab = 'cancelRequests'">CANCEL REQUESTS</button>
      <button v-if="loggedUser && loggedUser.role === 'MANAGER'" :class="{ active: currentTab === 'factoryOrders' }" @click="currentTab = 'factoryOrders'">FACTORY ORDERS</button>
    </div>
    <div class="tab-content" v-if="currentTab === 'profile'">
      <h2>Profile Information</h2>
      <form @submit.prevent="saveProfile">
        <label>
          Username:
          <input type="text" v-model="profile.username" required>
        </label>
        <label>
          First Name:
          <input type="text" v-model="profile.firstName" required>
        </label>
        <label>
          Last Name:
          <input type="text" v-model="profile.lastName" required>
        </label>
        <label>
          Gender:
          <select v-model="profile.gender" required>
            <option value="MALE">MALE</option>
            <option value="FEMALE">FEMALE</option>
          </select>
        </label>
        <label>
          Birthday:
          <input type="date" v-model="profile.birthday" required>
        </label>
        <div class="buttons">
          <button type="submit">Save</button>
          <button type="button" @click="changePasswordModalVisible = true">Change Password</button>
        </div>
      </form>
    </div>
    <div class="tab-content" v-if="currentTab === 'orders'">
      <div class="order" v-for="order in orders" :key="order.id">
        <div class="order-details">
          <div><strong>Order ID:</strong> {{ order.id }}</div>
          <div><strong>Date & Time:</strong> {{ formatDateTime(order.date) }}</div>
          <div><strong>Status:</strong> {{ order.status }}</div>
          <div><strong>Total Price:</strong> {{ order.price }} RSD</div>
        </div>
        <div class="button-column">
          <button @click="cancelOrder(order.id)" :disabled="order.status === 'CANCELLED'">
            Cancel
          </button>
          <button @click="goToOrderInfo(order.id)">
            Info
          </button>
        </div>
      </div>

    </div>
    <div class="tab-content" v-if="currentTab === 'cancelRequests'">
      <h2>Cancel Requests</h2>
      <table>
        <tr>
          <th>Request ID</th>
          <th>Order ID</th>
          <th>Date</th>
          <th>Status</th>
        </tr>
        <tr v-for="request in cancelRequests" :key="request.id">
          <td>{{ request.id }}</td>
          <td>{{ request.orderId }}</td>
          <td>{{ request.date }}</td>
          <td>{{ request.status }}</td>
        </tr>
      </table>
    </div>
    <div class="tab-content" v-if="currentTab === 'factoryOrders'">
      <h2>Factory Orders</h2>
      <div v-if="factoryOrders.length === 0">No orders found for your factory.</div>
      <div class="order" v-for="order in factoryOrders" :key="order.id">
        <div class="order-details">
          <div><strong>Order ID:</strong> {{ order.id }}</div>
          <div><strong>Date & Time:</strong> {{ formatDateTime(order.date) }}</div>
          <div><strong>Status:</strong> {{ order.status }}</div>
          <div><strong>Total:</strong> {{ order.price }}</div>
        </div>
        <div class="order-items">
          <div class="order-item" v-for="item in order.items" :key="item.id">
            <img :src="item.chocolate.image" alt="Chocolate Image" class="chocolate-logo">
            <div class="chocolate-info">
              <div><strong>{{ item.chocolate.name }}</strong></div>
              <div>Quantity: {{ item.quantity }}</div>
              <div>Price: {{ item.price }}</div>
            </div>
          </div>
        </div>
        <div class="order-actions">
              <button @click="approveOrder(order.id)" :disabled="order.status === 'APPROVED' || order.status === 'REJECTED'">Approve</button>
              <button @click="openRejectModal(order)" :disabled="order.status === 'REJECTED' || order.status === 'APPROVED'">Reject</button>
      </div>

      </div>
    </div>
  </div>

  <!-- Modal za reject -->
  <div class="modal" v-if="rejectModalVisible">
    <div class="modal-content">
      <span class="close" @click="rejectModalVisible = false">&times;</span>
      <h2>Reject Order</h2>
      <form @submit.prevent="rejectOrder(currentOrder, comment)" >
        <label>
          Reason for rejection:
          <textarea v-model="comment" required></textarea>
        </label>
        <button type="submit">Done</button>
      </form>
    </div>
  </div>


  <div class="modal" v-if="changePasswordModalVisible">
    <div class="modal-content">
      <span class="close" @click="changePasswordModalVisible = false">&times;</span>
      <h2>Change Password</h2>
      <form @submit.prevent="changePassword">
        <label>
          Current Password:
          <input type="password" v-model="currentPassword" required>
        </label>
        <div v-if="passwordChangeError">
          <span style="color: red;">{{ passwordChangeError }}</span>
        </div>
        <label>
          New Password:
          <input type="password" v-model="newPassword" required>
        </label>
        <label>
          Re-Type New Password:
          <input type="password" v-model="confirmPassword" required>
        </label>
        
        <div v-if="newPassword !== confirmPassword">
          <span style="color: red;">Passwords do not match.</span>
        </div>
        
        <button type="submit">Save</button>
      </form>
    </div>
  </div>

</template>

<script setup>
import { ref, onMounted , watch} from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const loggedUser = ref(null);
const profile = ref({});
const orders = ref([]);
const cancelRequests = ref([]);
const factoryOrders = ref([]);
const currentTab = ref('profile');
const changePasswordModalVisible = ref(false);
const rejectModalVisible = ref(false);
let currentPassword = ref('');
let newPassword = ref('');
let confirmPassword = ref('');
let passwordChangeError = ref('');
let comment = ref('');
let currentOrder = ref(null);

onMounted(() => {
  const storedUser = localStorage.getItem('loggedUser');
  if (storedUser) {
    loggedUser.value = JSON.parse(storedUser);
    console.log("Logged user:", loggedUser.value);

    loadOrders(loggedUser.value.id);

    if (currentTab.value === 'profile') {
      loadProfileData();
    }

    if (loggedUser.value.role === 'MANAGER') {
      loadFactoryOrders();
    }
  } else {
    console.error("No logged user found in localStorage.");
  }
});

const loadProfileData = async () => {
  if (!loggedUser.value) {
    console.error("Logged user is null.");
    return;
  }
  try {
    const response = await axios.get(`http://localhost:8080/chocolate-factory/rest/users/${loggedUser.value.id}`);
    console.log("Profile data loaded:", response.data);
    
    profile.value = {
      username: response.data.username,
      password: response.data.password,
      firstName: response.data.name,
      lastName: response.data.surname,
      gender: response.data.gender,
      birthday: response.data.dateOfBirth
    };
  } catch (error) {
    console.error("Failed to load profile data:", error);
  }
};

const saveProfile = async () => {
  if (!loggedUser.value) {
    console.error("Logged user is null.");
    return;
  }
  try {
    await axios.put(`http://localhost:8080/chocolate-factory/rest/users/${loggedUser.value.id}`, profile.value);
    console.log("Profile data saved successfully.");
    window.alert("Profile data saved successfully.");
  } catch (error) {
    console.error("Failed to save profile data:", error);
  }
};

const changePassword = async () => {
  if (!loggedUser.value) {
    console.error("Logged user is null.");
    return;
  }
  if (newPassword.value !== confirmPassword.value) {
    console.error("New passwords do not match.");
    return;
  }
  try {
    const response = await axios.put(`http://localhost:8080/chocolate-factory/rest/users/changePassword/${loggedUser.value.id}`, {
      currentPassword: currentPassword.value,
      newPassword: newPassword.value
    });
    console.log("Password changed successfully.", response.data);
    window.alert("Password changed successfully.");
    changePasswordModalVisible.value = false; 
  } catch (error) {
    if (error.response && error.response.status === 401) {
      passwordChangeError.value = "Current password is incorrect.";
    } else {
      console.error("Failed to change password:", error);
    }
  }
};

const cancelOrder = async (orderId) => {
  try {
    const response = await axios.put(`http://localhost:8080/chocolate-factory/rest/orders/cancel/${orderId}`);
    console.log("Order cancelled successfully:", response.data);
    window.alert("Order cancelled successfully.");
    loadOrders(loggedUser.value.id);
  } catch (error) {
    console.error("Failed to cancel order:", error);
  }
};

const goToOrderInfo = (orderId) => {
  router.push(`orderInfo/${orderId}`);
};

const loadOrders = async (userId) => {
  try {
    const response = await axios.get(`http://localhost:8080/chocolate-factory/rest/orders/byUserId/${userId}`);
    orders.value = response.data;

    for (const order of orders.value) {
      const itemsResponse = await axios.get(`http://localhost:8080/chocolate-factory/rest/order/${order.id}`);
      order.items = itemsResponse.data;

      for (const item of order.items) {
        if (item.chocolate) {
          item.chocolate = await getChocolateDetails(item.chocolate.id);
        }
      }
    }
  } catch (error) {
    console.error("Failed to load orders:", error);
  }
};

const loadFactoryOrders = () => {
  try {
    const managerId = loggedUser.value.id;

    axios.get(`http://localhost:8080/chocolate-factory/rest/orders/forManagersFactory/${managerId}`)
      .then(response => {
        factoryOrders.value = response.data;
      })
      .catch(error => console.error(error));
  } catch (error) {
    console.error("Failed to load factory orders:", error);
  }
};

const approveOrder = (orderId) => {
  axios.put(`http://localhost:8080/chocolate-factory/rest/orders/aprove/${orderId}`)
    .then(response => {
      console.log("Order approved successfully:", response.data);
      loadFactoryOrders();
      window.alert("Order approved successfully.");
    })
    .catch(error => {
      console.error("Failed to approve order:", error);
    });
};

const rejectOrder = () => {
  const storedComment = localStorage.getItem('rejectionComment');
  axios.put(`http://localhost:8080/chocolate-factory/rest/orders/reject/${currentOrder.value.id}`, {
    comment: storedComment // Pass the comment from local storage to the backend
  })
    .then(response => {
      console.log("Order rejected successfully:", response.data);
      rejectModalVisible.value = false;
      comment.value = ''; // Clear the rejection reason after submission
      localStorage.removeItem('rejectionComment'); // Clear the local storage item
      loadFactoryOrders();
      window.alert("Order rejected successfully.");
    })
    .catch(error => {
      console.error("Failed to reject order:", error);
    });
};

const openRejectModal = (order) => {
  currentOrder.value = order;
  rejectModalVisible.value = true;
  const storedComment = localStorage.getItem('rejectionComment');
  if (storedComment) {
    comment.value = storedComment; // Load the comment from local storage if it exists
  }
};

// Update the comment in local storage whenever it changes
watch(comment, (newValue) => {
  localStorage.setItem('rejectionComment', newValue);
});


const formatDateTime = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString();
};
</script>

<style>
.my-account {
  width: 800px;
  margin: 50px auto;
}

.tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.tabs button {
  padding: 10px 20px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: lightgray;
  flex: 1;
  text-align: center;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
}

.tabs button.active {
  background-color: #523F31;
  color: white;
}

.tab-content {
  padding: 20px;
}

form label {
  display: block;
  margin-bottom: 15px;
}

form label input,
form label select {
  display: block;
  margin-top: 5px;
  padding: 10px;
  width: calc(100% - 22px);
  border: 1px solid #ccc;
  border-radius: 5px;
}

button[type="submit"],
button[type="button"] {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #523F31;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
}

.order {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 15px;
  margin-bottom: 10px;
}

.order:hover {
  transform: scale(1.01);
}

.order-header {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 15px;
}

.order-items {
  display: flex;
  flex-wrap: wrap;
}

.order-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  width: 100%;
}

.order-actions button {
  background-color: #523F31;
  color: white;
  width: fit-content;
  font-size: small;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  padding: 10px 20px;
  margin: 5px;
}

.order-actions button:disabled {
  background-color: lightgray;
  cursor: not-allowed;
}

.order-actions button:hover {
  background-color: #634a38;
}

.order {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 15px;
  margin-bottom: 10px;
}

.order:hover {
  transform: scale(1.01);
}



.button-column button {
  background-color: #523F31;
  color: white;
  width: fit-content;
  font-size: small;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  padding: 10px 20px;
  margin: 5px;
}

.button-column button:hover {
  transform: scale(1.05);
  background-color: #634a38;
}

.chocolate-logo {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}

.chocolate-info {
  flex: 1;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

table, th, td {
  border: 1px solid black;
}

th, td {
  padding: 10px;
  text-align: left;
}

.modal {
  display: block;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 50%; 
  max-width: 400px; 
}

.close {
  color: #aaaaaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

.modal-content form label {
  display: block;
  margin-bottom: 15px;
}

.modal-content form label input {
  display: block;
  margin-top: 5px;
  padding: 8px; 
  width: calc(100% - 16px); 
  border: 1px solid #ccc;
  border-radius: 5px;
}

.modal-content form button[type="submit"] {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #523F31;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
}

.success-message {
  margin-top: 10px;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border-radius: 5px;
  text-align: center;
}
</style>
