import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/home/HomeView'
import BattleView from '@/views/battle/BattleView'
import NotFoundView from '@/views/error/NotFoundView'
import GameHistoryView from '@/views/gameHistory/GameHistoryView'
import GameHistoryReplayView from '@/views/gameHistory/GameHistoryReplayView'
import RankView from '@/views/rank/RankView'
import BotView from '@/views/bot/BotView'
import UserLoginView from '@/views/user/UserLoginView'
import UserRegisterView from '@/views/user/UserRegisterView'
import store from '@/store/index'

const routes = [
  {
    path: '/',
    name: 'HomeIndex',
    component: HomeView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: '/battle',
    name: 'BattleIndex',
    component: BattleView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/gamehistory',
    name: 'GameHistoryIndex',
    component: GameHistoryView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/gamehistory/:recordId',
    name: 'GameHistoryReplayIndex',
    component: GameHistoryReplayView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/rank',
    name: 'RankIndex',
    component: RankView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/user/login',
    name: 'LoginIndex',
    component: UserLoginView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: '/user/register',
    name: 'RegisterIndex',
    component: UserRegisterView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: '/user/bot',
    name: 'BotIndex',
    component: BotView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: '/notfound',
    name: 'NotFound',
    component: NotFoundView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/notfound',
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.isLogin)
    next({ name: 'LoginIndex' })
  else next()
})

export default router
