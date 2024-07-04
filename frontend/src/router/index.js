import { createRouter, createWebHashHistory } from 'vue-router';
import HelloWorld from '../components/HelloWorld.vue';
import AddChocolate from '../components/AddChocolate.vue';
import Factory from '../components/Factory.vue';
import Chocolate from '../components/Chocolate.vue';
import EditChocolate from '../components/EditChocolate.vue';
import CreateFactory from '../components/CreateFactory.vue';
import RegisteredUsers from '../components/RegisteredUsers.vue'
import Registration from '../components/Registration.vue';
import SignIn from '../components/SignIn.vue';
import MyAccount from '../components/MyAccount.vue';
import AddEmployee from '../components/AddEmployee.vue';
import ShoppingCart from '../components/ShoppingCart.vue';
import OrderInfo from '../components/OrderInfo.vue';

const routes = [
  {
    path: "/",
    name: "home",
    component: Factory,
  },
  
  {
    path: "/add-chocolate",
    name: "addChocolate",
    component: AddChocolate
  },
  {
    path: "/edit-chocolate/:chocolateId",
    name: "editChocolate",
    component: EditChocolate
  },
  {
    path: "/chocolates/:factoryId",
    name: "chocolates",
    component: Chocolate
  },
  
  {
    path: "/create-factory",
    name: "createFactory",
    component: CreateFactory
  },
  {
    path: "/my-account",
    name: "myAccount",
    component: MyAccount
  },
  {
    path: "/add-employee",
    name: "addEmployee",
    component: AddEmployee
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
  {
    path:"/factories",
    name:"factories",
    component: Factory,
  },
  {
    path: "/registered-users",
    name: "registeredUsers",
    component: RegisteredUsers,
  },
  {
    path: "/registration",
    name: "registration",
    component: Registration
  },
  {
    path: "/cart",
    name: "cart",
    component: ShoppingCart
  },{
    path: "/orderInfo/:orderId",
    name: "orderInfo",
    component: OrderInfo
  },
  {
    path: "/signIn",
    name: "signIn",
    component: SignIn
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
