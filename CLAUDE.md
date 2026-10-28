# goos-java

專案介紹見 [README.md](README.md)。

## TDD commit message 格式

照書中章節逐步先寫 test code 再寫 production code，commit message 用 Conventional Commits。定義、紅燈、綠燈、重構各自是獨立的 commit：

```
test(<scope>): define - <定義描述> [<書中出處>]
test(<scope>): red - <紅燈描述> [<書中出處>]
<type>(<scope>): green-step <n>/<m> - <綠燈途中描述> [<書中出處>]
<type>(<scope>): green - <綠燈描述> [<書中出處>]
refactor(<scope>): <重構描述> [<書中出處>]
```

- 定義 commit：只定義測項流程或證明測試工具鏈可以執行，能編譯、能執行、直接通過，不是紅燈
  - 第一個 e2e 測項：只寫測項流程與空殼 fixture，沒有任何斷言（參考 letterliu `5a72ed2`）
  - stair-step 測試：斷言只用來證明工具鏈能執行，所需的 jar 隨同一個 commit 加入
- 新增測項時，紅燈 commit 一次寫出該測項的**所有斷言**，不可分多個 commit 逐一加入斷言，也沒有「無斷言的測試骨架」的紅燈 commit
- 紅燈必須是「跑測試後的失敗」，不可以是編譯錯誤，也不可以是環境錯誤（沒有 display、連不上 Openfire 等）
- 紅燈 commit 只包含：
  - 該測項的測試程式碼與 test fixture
  - 為了通過編譯的最小 production code 定義
  - 該測項需要的 jar（vendor 進 `lib/`）
  - 編譯、執行這些程式碼所需的環境腳本調整（例如把新出現的 `src/` 與 `lib/deploy` 加入 classpath）
- 綠燈途中（`green-step`）：紅燈要多個步驟才能轉綠時，把綠燈拆成多個 commit，每個 `green-step` 只讓失敗點往後推進一格，測項在這些 commit 仍然是紅的
  - 最後一個 commit 用 `green`，測項在這個 commit 才全部轉綠
  - `<n>/<m>` 的 `<m>` 是 `green-step` 的總數，不含最後的 `green`
  - 每個 `green-step` 的失敗也必須是「跑測試後的失敗」，不可以是編譯錯誤或環境錯誤
  - 每個 `green-step` 只做讓目前失敗的斷言通過的最小實作
  - 測試傳入的值（例如 `main()` 的參數）直接使用，不在 production 重複寫死
- 綠燈 commit 用最簡單的實作讓紅燈轉綠，先寫死在既有類別內，不提前引入書中的新類別/interface
- 重構 commit 不改變行為，逐步把寫死的實作重構成書中的結構
- `<scope>`：測試層級（`unit`/`e2e`）或模組名（`ui`/`xmpp`...），哪個對這次改動更有辨識度就用哪個；跨很多模組時整個 scope 省略
- `<書中出處>`：章節（`ch10`）、小節（`3.6`）、頁碼（`p42`）可以視情況組合
- `define`/`red`/`green-step`/`green` 的 commit body 一定要加一行 `Test case: <測試案例名稱>`
- `green-step` 的 commit body 再加一行 `Still red at: <接下來失敗的斷言>`
- 整個 commit message 一律只能用英文，不能出現中文字

### infra 與文件 commit

Docker、環境腳本的改動（infra）與文件改動，都要跟定義/紅燈/綠燈/重構 commit 分開，各自獨立成 commit（紅燈 commit 內的 jar 與必要的腳本調整除外）：

- infra 依改動性質用 `build`/`feat`/`fix`，body 寫下這個 commit 的綠燈判準指令（一條完成前會失敗、完成後會成功的可執行指令），沒有 `Test case:` 這一行
- 文件用 `docs`
