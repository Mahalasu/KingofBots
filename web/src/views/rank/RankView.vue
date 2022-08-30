<template>
  <ContentField>
    <table class="table table-hover mb-0">
      <thead>
        <tr>
          <th>User</th>
          <th>Marks</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id" style="vertical-align: middle">
          <td>
            <img
              class="userAvatar"
              :src="'data:image/svg+xml;utf8,' + user.avatar"
              alt="avatar"
            />
            <span class="px-1">{{ user.username }}</span>
          </td>
          <td>
            {{ user.marks }}
          </td>
        </tr>
      </tbody>
    </table>
    <nav class="mt-3" aria-label="pagination">
      <ul class="pagination" style="float: right">
        <li class="page-item" @click="clickPage(-2)">
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li
          :class="'page-item ' + page.isActive"
          v-for="page in pageNumbers"
          :key="page.number"
          @click="clickPage(page.number)"
        >
          <a class="page-link" href="#">{{ page.number }}</a>
        </li>
        <li class="page-item" @click="clickPage(-1)">
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField'
import { useStore } from 'vuex'
import { ref } from 'vue'
import $ from 'jquery'
import { domainNameProd } from '@/global'

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore()
    let users = ref([])
    let currPage = 1
    let totalUsers = 0
    let pageNumbers = ref([])

    const clickPage = (page) => {
      if (page === -2) page = currPage - 1
      if (page === -1) page = currPage + 1
      // 10 players per page
      let maxPages = parseInt(Math.ceil(totalUsers / 10))
      if (page >= 1 && page <= maxPages) {
        getPageContent(page)
      }
    }

    const updatePageNumbers = () => {
      // 10 players per page
      let maxPages = parseInt(Math.ceil(totalUsers / 10))
      let newPageNumbers = []
      for (let i = currPage - 2; i <= currPage + 2; i++) {
        if (i >= 1 && i <= maxPages) {
          newPageNumbers.push({
            number: i,
            isActive: i === currPage ? 'active' : '',
          })
        }
      }
      pageNumbers.value = newPageNumbers
    }

    const getPageContent = (page) => {
      currPage = page
      $.ajax({
        type: 'get',
        url: `${domainNameProd}/api/rank/getlist`,
        data: {
          page,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          users.value = resp.users
          totalUsers = resp.usersCount
          updatePageNumbers()
        },
        error(resp) {
          console.log(resp)
        },
      })
    }

    getPageContent(currPage)

    return {
      users,
      pageNumbers,
      clickPage,
    }
  },
}
</script>

<style scoped>
.userAvatar {
  width: 50px;
  border-radius: 50%;
  border-style: dashed;
  border-width: 1px;
}
</style>
