<template>
  <div
    class="modal fade"
    id="createNewBot"
    tabindex="-1"
    data-bs-backdrop="static"
    aria-labelledby="createNewBotLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="createNewBotLabel">Create New Bot</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
            @click="reset"
          ></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            <label for="name" class="form-label">Name</label>
            <input
              v-model="botInfo.name"
              type="text"
              class="form-control"
              id="name"
              placeholder="Bot Name"
            />
          </div>
          <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea
              v-model="botInfo.description"
              class="form-control"
              id="description"
              rows="3"
              placeholder="Your Bots' Description"
            ></textarea>
          </div>
          <VAceEditor
            v-model:value="botInfo.code"
            lang="java"
            theme="chrome"
            style="height: 300px"
          />
          <div
            class="alert alert-warning d-flex align-items-center my-3 mx-auto"
            role="alert"
          >
            <div>
              <span class="fw-bold">Warning:</span> Please follow the
              <a
                target="_blank"
                rel="noopener noreferrer"
                href="https://github.com/Mahalasu/KingofBots#bot-creation-instruction"
                >instructions</a
              >
              when you are creating your own bot!
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <div class="errorMessage">{{ botInfo.errorMessage }}</div>
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
            @click="reset"
          >
            Close
          </button>
          <button type="button" class="btn btn-primary" @click="createBot">
            Create
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery'
import { useStore } from 'vuex'
import { reactive } from 'vue'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor'
import ace from 'ace-builds'
import { domainNameProd } from '@/global'

export default {
  components: {
    VAceEditor,
  },
  setup(props, context) {
    const store = useStore()
    const botInfo = reactive({
      name: '',
      description: '',
      code: '// please input your java code here',
      errorMessage: '',
    })

    ace.config.set(
      'basePath',
      'https://cdn.jsdelivr.net/npm/ace-builds@' +
        require('ace-builds').version +
        '/src-noconflict/',
    )

    const createBot = () => {
      $.ajax({
        url: `${domainNameProd}/api/user/bot/create`,
        type: 'post',
        data: {
          name: botInfo.name,
          description: botInfo.description,
          code: botInfo.code,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          if (resp.errorMessage === 'success') {
            Modal.getInstance('#createNewBot').hide()
            reset()
            context.emit('created')
          } else botInfo.errorMessage = resp.errorMessage
        },
      })
    }

    const reset = () => {
      botInfo.name = ''
      botInfo.description = ''
      botInfo.code = '// please input your java code here'
      botInfo.errorMessage = ''
    }

    return {
      createBot,
      botInfo,
      reset,
    }
  },
}
</script>

<style scoped>
.errorMessage {
  color: red;
}
</style>
