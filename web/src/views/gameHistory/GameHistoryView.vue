<template>
  <ContentField>
    <table class="table table-hover mb-0">
      <thead>
        <tr>
          <th>Player 1</th>
          <th>Player 2</th>
          <th>Result</th>
          <th>VS Time</th>
          <th>Operation</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="record in records"
          :key="record.gameHistory.id"
          style="vertical-align: middle"
        >
          <td>
            <img
              class="userAvatar"
              :src="'data:image/svg+xml;utf8,' + record.aAvatar"
              alt="avatar"
            />
            <span class="px-1">{{ record.aUsername }}</span>
          </td>
          <td>
            <img
              class="userAvatar"
              :src="'data:image/svg+xml;utf8,' + record.bAvatar"
              alt="avatar"
            />
            <span class="px-1">{{ record.bUsername }}</span>
          </td>
          <td>
            {{
              record.gameHistory.loser === 'both'
                ? 'Draw'
                : record.gameHistory.loser === 'a'
                ? 'Player1 Wins'
                : 'Player2 Wins'
            }}
          </td>
          <td>{{ record.gameHistory.createTime }}</td>
          <td>
            <button
              @click="openRecordReplay(record.gameHistory.id)"
              type="button"
              class="btn btn-danger"
            >
              Replay
            </button>
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
import router from '@/router/index'
import $ from 'jquery'
import { domainNameProd } from '@/global'

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore()
    let records = ref([])
    let currPage = 1
    let totalRecords = 0
    let pageNumbers = ref([])

    const clickPage = (page) => {
      if (page === -2) page = currPage - 1
      if (page === -1) page = currPage + 1
      // 10 records per page
      let maxPages = parseInt(Math.ceil(totalRecords / 10))
      if (page >= 1 && page <= maxPages) {
        getPageContent(page)
      }
    }

    const updatePageNumbers = () => {
      // 10 records per page
      let maxPages = parseInt(Math.ceil(totalRecords / 10))
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
        url: `${domainNameProd}/api/gamehistory/getlist`,
        data: {
          page,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          records.value = resp.allHistory
          totalRecords = resp.pageCount
          updatePageNumbers()
        },
        error(resp) {
          console.log(resp)
        },
      })
    }

    getPageContent(currPage)

    const string2Map = (str) => {
      let map = []
      for (let i = 0, k = 0; i < 13; i++) {
        let line = []
        for (let j = 0; j < 14; j++, k++) {
          if (str[k] === '0') line.push(0)
          else line.push(1)
        }
        map.push(line)
      }
      return map
    }

    const openRecordReplay = (recordId) => {
      for (const record of records.value) {
        if (record.gameHistory.id === recordId) {
          store.commit('updateIsRecord', true)
          const gameMap = string2Map(record.gameHistory.map)
          store.commit('updateGame', {
            gameMap,
            playerAId: record.gameHistory.aId,
            playerAsx: record.gameHistory.aSx,
            playerAsy: record.gameHistory.aSy,
            playerBId: record.gameHistory.bId,
            playerBsx: record.gameHistory.bSx,
            playerBsy: record.gameHistory.bSy,
          })
          store.commit('updateSteps', {
            aSteps: record.gameHistory.aSteps,
            bSteps: record.gameHistory.bSteps,
          })
          store.commit('updateRecordLoser', record.gameHistory.loser)
          router.push({
            name: 'GameHistoryReplayIndex',
            params: {
              recordId,
            },
          })
          break
        }
      }
    }

    return {
      records,
      openRecordReplay,
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
