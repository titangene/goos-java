# Docker 環境

## Java

Java 的編譯、執行、測試一律透過 `docker-compose.yml` 定義的 `toolbox` container 執行，不在 host 直接跑 `javac`/`java`。

| 腳本 | 用途 |
|---|---|
| `scripts/test.sh` | 編譯並執行 E2E 測試（`test.sh --headed` 會讓測試執行時的 Swing UI 顯示在主機螢幕上，方便除錯；預設不加參數是用 `xvfb-run` 虛擬 display，畫面不會顯示出來） |

## Openfire（XMPP）

E2E 測試需要連到真的 XMPP server，跟 goos-code 一樣用 Docker 跑 [Openfire](https://ghcr.io/igniterealtime/openfire)：`docker-compose.yml` 裡 `toolbox` 用 `network_mode: service:openfire`，讓 container 內的 `localhost` 直接連到 openfire（測試/production code 裡寫死 `XMPP_HOSTNAME = "localhost"`，不用改 source code）。

Openfire 第一次啟動要跑過 setup wizard、建立測試帳號才能用，透過 `setup-openfire.js` 用 Playwright 自動配置 wizard 並建立 3 組測試帳號 (因此流程沒有對應的 API 可用，只能透過瀏覽器自動化腳本來處理)：

| 帳號 | 密碼 |
|---|---|
| `sniper` | `sniper` |
| `auction-item-54321` | `auction` |
| `auction-item-65432` | `auction` |

### 第一次建立環境（或 Openfire 資料被清空後）

```bash
docker/scripts/start-env.sh
```

會依序：
1. 啟動 openfire，等 9090 有回應
2. build/啟動 toolbox
3. 安裝 Playwright（在 `docker/` 目錄執行 `npm install`）
4. 跑 `setup-openfire.js` 建立帳號

之後就能直接跑 `docker/scripts/test.sh`。

### 停止環境

停止並移除 container（openfire + toolbox），並清空 Openfire 的資料 volume。

```bash
docker/scripts/stop-env.sh
```

### 環境設定壞掉、想砍掉重建

```bash
docker/scripts/reset-env.sh
```

等於 `stop-env.sh` 接 `start-env.sh`，會清空 Openfire 資料重新跑一次 wizard。
