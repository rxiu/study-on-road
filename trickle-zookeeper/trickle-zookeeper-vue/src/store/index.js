import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
  user: {
    username: JSON.parse(sessionStorage.getItem('user') || '{}').username || '',
    isLogin: sessionStorage.getItem("isLogin") || false
  }
}

const getters = {
  getUser: state => state.user.username || '',
  isLogin: state => state.user.isLogin || false
}

const mutations = {
  saveUser: function (state, user) {
    state.user = user;
    state.user.isLogin = true;
    sessionStorage.setItem('user', JSON.stringify(user));
  }
}

const actions = {
  commitSaveUser: ({commit}, user) => commit('saveUser', user)
}

const store = new Vuex.Store({
  state, getters, mutations, actions
})

export default store
