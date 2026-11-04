# goos-java

參考 [GOOS（Growing Object-Oriented Software, Guided by Tests）](https://www.growing-object-oriented-software.com/) 書中的 Auction Sniper 範例，重新用 TDD 刻一次，盡量比照作者原始的 [goos-code](https://github.com/sf105/goos-code) repo 的簡單管理方式：依賴用 vendor jar、不用額外的建置工具（不用 Maven/Gradle）。

`baby-step-v2` 分支參考 [letterliu/goos-java-practice](https://github.com/letterliu/goos-java-practice/commits/tdd-baby-step/) 的節奏：紅燈、綠燈、重構各自是獨立的 commit，環境與依賴只在當下真正需要時才加入。

## 使用方式

### 需要的環境

- Docker
- Node.js（含 npm）與 curl：第一次執行時，用來自動設定 Openfire（XMPP server）
- 圖形介面（X display）：第一次執行時會開 Chromium 視窗

不用在本機安裝 Java，編譯、執行和測試都在 container 裡跑

### 1. 跑測試

```bash
scripts/test.sh
```

編譯 app 後執行 E2E 測試，畫面不會顯示出來。每次都會重新編譯，一定是跑最新的程式碼

第一次跑會先自動準備環境，之後就不會再跑這段：

1. 安裝 Playwright
2. 啟動 Openfire
3. 用 Playwright 跑完 Openfire 的 setup wizard 並建立測試帳號，會開 Chromium 視窗（這件事沒有 API 能直接做，只能透過瀏覽器自動化）
4. 啟動編譯和跑測試用的 container，image 不存在時會先 build

準備環境失敗或被中斷（例如按 Ctrl-C）時會自動清掉 Openfire 資料，再跑一次即可

可用的測試帳號：

| 帳號 | 密碼 |
|---|---|
| `sniper` | `sniper` |
| `auction-item-54321` | `auction` |
| `auction-item-65432` | `auction` |

Openfire 的 resource conflict policy 設成 `Never kick`：同一組帳號 + resource 重複登入時會拒絕新的登入，回 `conflict(409)`，對應 GOOS ch12 的情境（Openfire 預設是 `Always kick`）

### 2. 管理環境

平常不用管理環境，`scripts/test.sh` 會自動啟動。只有下面這些情況要手動處理

改了 `toolbox.Dockerfile` 後，重新 build：

```bash
scripts/env.sh start
```

環境設定壞掉、想砍掉重建（會重新 build 並重跑 setup wizard）：

```bash
scripts/env.sh reset
```

不再使用時，移除 container、image 與 Openfire 資料，否則會留在 Docker 裡：

```bash
scripts/env.sh stop
```

要開 Openfire admin console 時，host port 是由 Docker 分配的，用這個指令查詢網址中要用的 port：

```bash
docker compose port openfire 9090
```

它只會輸出一行，例如 `0.0.0.0:33372`，把最後的 port 接在 `http://localhost:` 後面就是 Openfire admin console 的網址
