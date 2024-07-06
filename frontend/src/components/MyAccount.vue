<template>
  <div class="my-account">
    <div class="content">
      <div class="sidebar" v-if="currentTab === 'orders'">
        <div class="search-container">
          <h4>SEARCH ORDERS</h4>
          <div class="search-bar">
            <input v-model="searchCriteria.factoryName" type="text" placeholder="Factory Name" />
            <div class="inline-inputs">
              <input v-model="searchCriteria.minPrice" type="number" placeholder="Min Price" />
              <input v-model="searchCriteria.maxPrice" type="number" placeholder="Max Price" />
            </div>
            <div class="inline-inputs">
              <input v-model="searchCriteria.startDate" type="date" placeholder="Start Date" />
              <input v-model="searchCriteria.endDate" type="date" placeholder="End Date" />
            </div>
          </div>
          <div class="buttons">
            <button class="btn btn-search" @click="searchOrders">Search</button>
            <button class="btn btn-clear" @click="clearSearch">Clear</button>
          </div>
        </div>
        <div class="separator"></div>
        <div class="sort-container">
          <h4>SORT ORDERS</h4>
          <select v-model="sortCriteria.sortBy" @change="applySorting">
            <option value="">Default Sorting</option>
            <option value="name-asc">Sort by Factory Name: Ascending</option>
            <option value="name-desc">Sort by Factory Name: Descending</option>
            <option value="price-asc">Sort by Price: Ascending</option>
            <option value="price-desc">Sort by Price: Descending</option>
            <option value="date-asc">Sort by Date: Ascending</option>
            <option value="date-desc">Sort by Date: Descending</option>
          </select>
        </div>
      </div>
      <div class="sidebar" v-if="currentTab === 'factoryOrders'">
        <div class="search-container">
          <h4>SEARCH FACTORY ORDERS</h4>
          <div class="search-bar">
            <div class="inline-inputs">
              <input v-model="factoryOrderSearchCriteria.minPrice" type="number" placeholder="Min Price" />
              <input v-model="factoryOrderSearchCriteria.maxPrice" type="number" placeholder="Max Price" />
            </div>
            <div class="inline-inputs">
              <input v-model="factoryOrderSearchCriteria.startDate" type="date" placeholder="Start Date" />
              <input v-model="factoryOrderSearchCriteria.endDate" type="date" placeholder="End Date" />
            </div>
          </div>
          <div class="buttons">
            <button class="btn btn-search" @click="searchFactoryOrders">Search</button>
            <button class="btn btn-clear" @click="clearFactoryOrderSearch">Clear</button>
          </div>
        </div>
        <div class="separator"></div>
        <div class="sort-container">
          <h4>SORT FACTORY ORDERS</h4>
          <select v-model="factoryOrderSortCriteria.sortBy" @change="sortFactoryOrders">
            <option value="">Default Sorting</option>
            <option value="price-asc">Sort by Price: Ascending</option>
            <option value="price-desc">Sort by Price: Descending</option>
            <option value="date-asc">Sort by Date: Ascending</option>
            <option value="date-desc">Sort by Date: Descending</option>
          </select>
        </div>
      </div>
      <div class="main-content">
        <div class="tabs">
          <button :class="{ active: currentTab === 'profile' }" @click="currentTab = 'profile'">MY PROFILE</button>
          <button v-if="loggedUser && loggedUser.role === 'CUSTOMER'" :class="{ active: currentTab === 'orders' }" @click="currentTab = 'orders'">MY ORDERS</button>
          <button v-if="loggedUser && loggedUser.role === 'MANAGER'" :class="{ active: currentTab === 'factoryCustomers' }" @click="currentTab = 'factoryCustomers'">FACTORY CUSTOMERS</button>
          <button v-if="loggedUser && loggedUser.role === 'MANAGER'" :class="{ active: currentTab === 'factoryOrders' }" @click="currentTab = 'factoryOrders'">FACTORY ORDERS</button>
        </div>
        <div v-if="currentTab === 'profile'">
          <form @submit.prevent="saveProfile">
            <label>
              <br/>
              Points: 
              <input type="number" v-model="profile.points" readonly>
            </label>
            <label>
              Username:
              <input type="text" v-model="profile.username" required>
            </label>
            <label>
              First Name:
              <input type="text" v-model="profile.name" required>
            </label>
            <label>
              Last Name:
              <input type="text" v-model="profile.surname" required>
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
              <input type="date" v-model="profile.dateOfBirth" required>
            </label>
            <div class="buttons">
              <button type="submit">Save</button>
              <button type="button" @click="changePasswordModalVisible = true">Change Password</button>
            </div>
          </form>
        </div>
        <div class="tab-content" v-if="currentTab === 'orders'">
          <div class="orders-list">
            <div class="order" v-for="order in sortedOrders" :key="order.id">
              <div class="order-details">
                <div><strong>Factory:</strong> {{ order.factory.name }}</div>
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
        </div>
        <div class="tab-content" v-if="currentTab === 'factoryCustomers'">
          <div v-if="factoryCustomers.length === 0">No customers found for your factory.</div>
          <table v-else>
            <tr>
              <th>Username</th>
              <th>Name</th>
              <th>Surname</th>
            </tr>
            <tr v-for="customer in factoryCustomers" :key="customer.id">
              <td>{{ customer.username }}</td>
              <td>{{ customer.name }}</td>
              <td>{{ customer.surname }}</td>
            </tr>
          </table>
        </div>
        <div class="tab-content" v-if="currentTab === 'factoryOrders'">
          <div v-if="factoryOrders.length === 0">No orders found for your factory.</div>
          <div class="order" v-for="order in sortedFactoryOrders" :key="order.id">
            <div class="order-details">
              <div><strong>Order ID:</strong> {{ order.id }}</div>
              <div><strong>Date & Time:</strong> {{ formatDateTime(order.date) }}</div>
              <div><strong>Status:</strong> {{ order.status }}</div>
              <div><strong>Total Price:</strong> {{ order.price }}</div>
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
    </div>
  </div>

  <!-- Modal za reject -->
  <div class="modal" v-if="rejectModalVisible">
    <div class="modal-content">
      <span class="close" @click="rejectModalVisible = false">&times;</span>
      <h2>Reject Order</h2>
      <form @submit.prevent="rejectOrder(currentOrder, comment)">
        <label>
          Reason for Rejection:
          <textarea v-model="comment" required class="reject-reason-textarea"></textarea>
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
import { ref, onMounted, watch, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const loggedUser = ref(null);
const profile = ref({
  username: '',
  password: '',
  name: '',
  surname: '',
  gender: '',
  dateOfBirth: '',
  points: ''  
});
const orders = ref([]);
const cancelRequests = ref([]);
const factoryOrders = ref([]);
const factoryCustomers = ref([]);
const currentTab = ref('profile');
const changePasswordModalVisible = ref(false);
const rejectModalVisible = ref(false);
let currentPassword = ref('');
let newPassword = ref('');
let confirmPassword = ref('');
let passwordChangeError = ref('');
let comment = ref('');
let currentOrder = ref(null);

const searchCriteria = ref({
  factoryName: '',
  minPrice: '',
  maxPrice: '',
  startDate: '',
  endDate: ''
});

const sortCriteria = ref({
  sortBy: '',
});

const factoryOrderSearchCriteria = ref({
  minPrice: '',
  maxPrice: '',
  startDate: '',
  endDate: ''
});

const factoryOrderSortCriteria = ref({
  sortBy: '',
});

onMounted(() => {
    const storedUser = localStorage.getItem('loggedUser');
    if (storedUser) {
        loggedUser.value = JSON.parse(storedUser);
        console.log("Logged user:", loggedUser.value);

        if (loggedUser.value.role === 'MANAGER') {
            loadFactoryOrders();
            loadFactoryCustomers();
            if (!loggedUser.value.factoryId) {
                loadFactoryIdForManager(loggedUser.value.id);
            }
        }

        loadOrders(loggedUser.value.id);

        if (currentTab.value === 'profile') {
            loadProfileData();
        }
    } else {
        console.error("No logged user found in localStorage.");
    }
});

const loadFactoryIdForManager = async (managerId) => {
  try {
    const response = await axios.get(`http://localhost:8080/chocolate-factory/rest/factories/byManager/${managerId}`);
    loggedUser.value.factoryId = response.data.id;
    console.log("Loaded factory ID:", response.data.id);
  } catch (error) {
    console.error("Failed to load factory ID for manager:", error);
  }
};

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
      name: response.data.name,
      surname: response.data.surname,
      gender: response.data.gender,
      dateOfBirth: response.data.dateOfBirth,
      points: response.data.points  
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
      const itemsResponse = await axios.get(`http://localhost:8080/chocolate-factory/rest/orders/${order.id}`);
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

const loadFactoryCustomers = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/chocolate-factory/rest/orders/factoryCustomers/${loggedUser.value.id}`);
        factoryCustomers.value = response.data;
        console.log("Loaded factory customers:", factoryCustomers.value);
    } catch (error) {
        console.error("Failed to load factory customers:", error);
    }
};

const approveOrder = (orderId) => {
  axios.put(`http://localhost:8080/chocolate-factory/rest/orders/approve/${orderId}`)
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
    comment: storedComment
  })
    .then(response => {
      console.log("Order rejected successfully:", response.data);
      rejectModalVisible.value = false;
      comment.value = '';
      localStorage.removeItem('rejectionComment');
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
    comment.value = storedComment;
  }
};

watch(comment, (newValue) => {
  localStorage.setItem('rejectionComment', newValue);
});

const searchOrders = async () => {
  try {
    const params = new URLSearchParams();
    if (searchCriteria.value.factoryName) params.append('factoryName', searchCriteria.value.factoryName);
    if (searchCriteria.value.minPrice) params.append('minPrice', searchCriteria.value.minPrice);
    if (searchCriteria.value.maxPrice) params.append('maxPrice', searchCriteria.value.maxPrice);
    if (searchCriteria.value.startDate) params.append('startDate', searchCriteria.value.startDate);
    if (searchCriteria.value.endDate) params.append('endDate', searchCriteria.value.endDate);

    const response = await axios.get(`http://localhost:8080/chocolate-factory/rest/orders/search`, { params });
    orders.value = response.data;
  } catch (error) {
    console.error("Failed to search orders:", error);
  }
};

const clearSearch = () => {
  searchCriteria.value = {
    factoryName: '',
    minPrice: '',
    maxPrice: '',
    startDate: '',
    endDate: ''
  };
  loadOrders(loggedUser.value.id);
};

const searchFactoryOrders = async () => {
  if (!loggedUser.value.factoryId) {
    console.error("Factory ID is not defined.");
    return;
  }

  try {
    const params = new URLSearchParams();
    params.append('factoryId', loggedUser.value.factoryId);
    if (factoryOrderSearchCriteria.value.minPrice) params.append('minPrice', factoryOrderSearchCriteria.value.minPrice);
    if (factoryOrderSearchCriteria.value.maxPrice) params.append('maxPrice', factoryOrderSearchCriteria.value.maxPrice);
    if (factoryOrderSearchCriteria.value.startDate) params.append('startDate', factoryOrderSearchCriteria.value.startDate);
    if (factoryOrderSearchCriteria.value.endDate) params.append('endDate', factoryOrderSearchCriteria.value.endDate);

    const response = await axios.get('http://localhost:8080/chocolate-factory/rest/orders/searchFactoryOrders', { params });
    factoryOrders.value = response.data;
  } catch (error) {
    console.error("Failed to search factory orders:", error);
  }
};

const clearFactoryOrderSearch = () => {
  factoryOrderSearchCriteria.value = {
    minPrice: '',
    maxPrice: '',
    startDate: '',
    endDate: ''
  };
  loadFactoryOrders();
};

const formatDateTime = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString();
};

const applySorting = async () => {
  try {
    const sortBy = sortCriteria.value.sortBy;
    let order = 'asc';
    if (sortBy.includes('-desc')) {
      order = 'desc';
    }
    const response = await axios.get('http://localhost:8080/chocolate-factory/rest/orders/sort', {
      params: {
        sortBy: sortBy.replace('-asc', '').replace('-desc', ''),
        order: order
      }
    });
    orders.value = response.data;
  } catch (error) {
    console.error("Failed to sort orders:", error);
  }
};

const sortFactoryOrders = async () => {
  if (!loggedUser.value.factoryId) {
    console.error("Factory ID is not defined.");
    return;
  }

  try {
    const factoryId = loggedUser.value.factoryId;
    const sortBy = factoryOrderSortCriteria.value.sortBy || '';
    let order = 'asc';
    if (sortBy.includes('-desc')) {
      order = 'desc';
    }

    console.log("Sorting Parameters:", { factoryId, sortBy, order });

    const response = await axios.get('http://localhost:8080/chocolate-factory/rest/orders/sortFactoryOrders', {
      params: {
        factoryId: factoryId,
        sortBy: sortBy.replace('-asc', '').replace('-desc', ''),
        order: order
      }
    });

    console.log("Sorted Factory Orders:", response.data);
    factoryOrders.value = response.data;
  } catch (error) {
    console.error("Failed to sort factory orders:", error);
    factoryOrders.value = [];
  }
};

const sortedOrders = computed(() => {
  if (!sortCriteria.value.sortBy) {
    return orders.value;
  }
  const [sortBy, order] = sortCriteria.value.sortBy.split('-');
  return [...orders.value].sort((a, b) => {
    let compareA, compareB;
    if (sortBy === 'name') {
      compareA = a.factory.name.toLowerCase();
      compareB = b.factory.name.toLowerCase();
    } else if (sortBy === 'price') {
      compareA = a.price;
      compareB = b.price;
    } else if (sortBy === 'date') {
      compareA = new Date(a.date);
      compareB = new Date(b.date);
    }
    if (compareA < compareB) return order === 'asc' ? -1 : 1;
    if (compareA > compareB) return order === 'asc' ? 1 : -1;
    return 0;
  });
});

const sortedFactoryOrders = computed(() => {
  if (!factoryOrderSortCriteria.value.sortBy) {
    return factoryOrders.value;
  }
  const [sortBy, order] = factoryOrderSortCriteria.value.sortBy.split('-');
  return [...factoryOrders.value].sort((a, b) => {
    let compareA, compareB;
    if (sortBy === 'price') {
      compareA = a.price;
      compareB = b.price;
    } else if (sortBy === 'date') {
      compareA = new Date(a.date);
      compareB = new Date(b.date);
    }
    if (compareA < compareB) return order === 'asc' ? -1 : 1;
    if (compareA > compareB) return order === 'asc' ? 1 : -1;
    return 0;
  });
});
</script>

<style>
.my-account {
  width: 1000px;
  margin: 50px auto;
  display: flex;
  justify-content: center;
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

.content {
  display: flex;
  width: 100%;
}

.sidebar {
  width: 250px;
  padding: 20px;
  margin-right: 10px;
  margin-left: -100px; 
}

.search-container {
  margin-bottom: 20px;
  width: 100%;
  margin-left: -30px;
}

.search-bar {
  display: flex;
  flex-direction: column;
}

.inline-inputs {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.inline-inputs input {
  flex: 1;
}

.search-bar input {
  width: calc(100% - 16px);
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.buttons {
  display: flex;
  justify-content: center;
}

.buttons .btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
}

.buttons .btn-search {
  background-color: #523F31;
  color: white;
}

.buttons .btn-clear {
  background-color: #523F31;
  color: white;
}

.main-content {
  flex: 1;
  max-width: 1000px; 
}

form label {
  display: block;
  margin-bottom: 15px;
}

form label input,
form label select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

input[type="number"] {
  width: 100%;
  text-align: center;
  padding: 8px;
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

table th {
  background-color: #523F31;
  color: white;
  font-weight: bold;
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

.modal-content form label input,
.modal-content form label textarea {
  display: block;
  margin-top: 5px;
  padding: 8px; 
  width: calc(100% - 16px); 
  border: 1px solid #ccc;
  border-radius: 5px;
}

.modal-content form label textarea.reject-reason-textarea {
  height: 100px; /* Visina textarea polja */
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

.sort-container h4 {
  text-align: left;
  margin-bottom: 5px;
  font-weight: 500;
  font-size: 0.9rem;
}

.sort-container select {
  font-size: 1rem;
  padding: 8px;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-top: 10px;
}

.sort-container {
  margin-left: -30px;
}

.separator {
  border-bottom: 1px solid #ddd; 
  padding-bottom: 10px; 
  margin-bottom: 10px; 
  margin-left: -30px;
}
</style>
