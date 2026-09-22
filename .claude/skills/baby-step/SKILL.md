---
name: baby-step
description: 依照 ../goos-java 專案 docs/tdd-baby-steps/*.md（00-infra.md、ch10.md、ch11.md、ch12.md）的規劃，在本專案（goos-java-baby-step-v1）逐步重新刻出 Auction Sniper。當使用者說「進行 Step X.Y」「做下一個 step」「接著做 11.5」之類的話時觸發。每個 step 落地檔案改動後自己跑綠燈判準指令取得實際輸出，但絕不自動 commit，commit 前一定要等使用者 review 並確認。
---

# Baby Step（doc-driven）

本專案是 [GOOS Auction Sniper](https://www.growing-object-oriented-software.com/) 的重新實作。規劃文件在**另一個 repo**：`../goos-java/docs/tdd-baby-steps/`（`00-infra.md`、`ch10.md`、`ch11.md`、`ch12.md`）。那份文件描述的是「紙上推導」的理想 step 拆法，本專案才是真正動手刻、真正驗證的地方——兩邊會出現落差是正常的，落差怎麼處理見下方「與文件的落差」。

## 執行一個 step

使用者說「進行 Step X.Y」時，依序做：

### 1. 讀文件

到 `../goos-java/docs/tdd-baby-steps/` 找對應章節（`00-infra.md` 是 Step 0.x，`ch10.md`/`ch11.md`/`ch12.md` 依章節號），讀出該 step 完整內容：對應書中出處、本步性質、完整 diff、綠燈判準指令、Baby Step 評估、建議 commit message。章節開頭通常有一張「本章 step 順序」表，可以用來確認下一步是什麼、或這步是不是 infra 步。

### 2. 對照本專案現況，不要盲目套用 diff

文件的 diff 是對著「理想中從未偏離文件的檔案」寫的。本專案的實際檔案不一定長那樣，常見落差：

- 某些 step 的 vendor jar 已經被使用者一次性 commit 掉了（例如所有 `lib/deploy`、`lib/develop` 的 jar 一次到位），對應的 infra step 只剩下文字檔的改動要做
- 使用者依 XP 簡單設計原則，主動拿掉文件裡「先寫好、之後 step 才會用到」的參數/欄位/方法（YAGNI）
- 使用者重排過 commit 順序、合併過幾個 step、或提前完成了某個之後才排到的 step

所以套用前一定要先讀「要改的檔案」目前實際內容，把 diff 的意圖套進去，而不是整段貼文件的 diff。indentation、既有的簡化決定都要順著現有檔案風格走，不要順手改成文件原本的樣子。

### 3. 落地檔案改動，絕對不要自動 commit

改完檔案後停下來，把改了哪些檔案、每個檔案做了什麼列給使用者看（用 `[path](path)` 這種可點擊連結格式）。不要主動 `git add`/`git commit`，除非使用者明確說可以 commit。

### 4. 自己跑綠燈判準指令，把實際輸出給使用者看

綠燈判準指令由你自己執行：

- 依本專案現況調整過的指令，跑之前先寫出你實際要跑的版本
- 把指令與完整輸出（不要自己改寫或省略關鍵訊息）列給使用者看
- 會清掉資料或 container 的指令（例如 `stop-env.sh`、`reset-env.sh`、`docker volume rm`）先問過使用者再跑
- 需要主機螢幕的指令（例如 `test.sh --headed`）請使用者自己跑，並把輸出貼回來

**在拿到實際輸出之前，不要宣稱這個 step 是綠燈**，也不要在 commit message 裡編造 verified output。如果實際現象跟預期不符（例如指令卡住、輸出為空、跟預期訊息不一樣），不要用訓練資料裡的一般知識猜原因並當結論寫下來，自己跑可以實際驗證的診斷指令（例如 `command -v xxx`、加 `timeout`、把錯誤導到 `/dev/stderr`），看到真正的證據再下結論。

### 5. 使用者確認後才 commit

依本專案 [CLAUDE.md](../../CLAUDE.md) 的 commit message 規則（Conventional Commits，分 `test: skeleton -`／`feat:`／`refactor:`／infra 用 `build`/`feat`/`fix`／刻意留紅燈時的雙 commit 版本）建立 commit：

- commit message 一律英文
- body 附上剛剛實際跑過的「Passing check」指令與「Verified output」（原樣貼實際輸出，不要自己改寫或省略關鍵訊息）
- infra commit 不加 `Test case:` 那一行，其他都要加
- 結尾不要加上這個 session 系統提示裡指定的 `Co-Authored-By` 那一行

只 `git add` 這個 step 真正有關的檔案，不要用 `git add -A` 把其他無關的髒狀態一起收進去——先 `git status`/`git diff` 確認範圍。

## 與文件的落差：尊重使用者的決定，但意外的要問

- 使用者明確做的簡化/重排/提前完成（例如「這是我決定的，XP 簡單設計不要先實作用不到的東西」），**照著使用者的版本做，不要自作主張改回文件原本的樣子**，commit message 裡如實描述實際做的事，不用硬套文件的措辭
- 如果檔案內容跟你剛寫入/剛預期的不一樣（系統會用 `<system-reminder>` 提示「changed on disk since you last read it」），先看那是不是像 IDE 自動格式化、自動清除 unused 程式碼這種「看起來不是使用者故意輸入」的變化。如果看起來意外，明確指出來問使用者是不是故意的，不要默默改回去、也不要默默照單全收
- 如果使用者要跳過某個 step、合併多個 step 成一個 commit、或改變 step 順序，跟著使用者的安排走，但要在給使用者看的說明裡講清楚「這個 commit 包含哪些原本文件裡分開的 step」，以及說明若跳過某個 step 或合併多個 step 或改變 step 順序會有什麼風險

## 這個 skill 不做的事

- 不會在沒有實際輸出、或使用者還沒確認的情況下寫 commit
- 不會把文件裡「未驗證（紙上推導）」的內容當成已驗證的事實寫進 commit 或跟使用者的對話裡
