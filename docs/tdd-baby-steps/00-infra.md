# 00-infra：孤兒分支起點到第一個 E2E 測項之前

從空白的孤兒分支開始，建立 `README.md`、`CLAUDE.md`、toolbox container、stair-step 測試、E2E 測試腳本與 IDE 設定。第一個書中測項需要的 display、Openfire、smack、WindowLicker 放在 [ch10.md](ch10.md)

---

## docs：[3b9ff3e](https://github.com/titangene/goos-java/commit/3b9ff3ef9dc4d0ead1bcb5630d3e9ff2a53c2ee8) 建立 README

- commit message：`docs: add README`
- 對應 `main` 分支：[48211cd](https://github.com/titangene/goos-java/commit/48211cd729f4efe1688c3144984a2381f7082de4)

---

## docs：[007a47a](https://github.com/titangene/goos-java/commit/007a47a224781892e074bbded948e95bde3426fb) 建立 CLAUDE.md，定義 define/red/green/refactor 的 commit 格式

- commit message：`docs: add CLAUDE.md with the define/red/green/refactor commit format`
- 對應 `main` 分支：[d8e8181](https://github.com/titangene/goos-java/commit/d8e81814b58413df965b1b865cfe5402c7dc012b)（部分）、[5e09274](https://github.com/titangene/goos-java/commit/5e092742fc6fcbf6f65dc986bc112e9db613c1ee)（部分）

---

## infra：[e9638b6](https://github.com/titangene/goos-java/commit/e9638b63b935628c88c2c6c9f4716ecbdd7a7503) 新增只有 JDK 8 的 toolbox container

- commit message：

  ```text
  build: add JDK 8 toolbox container

  Green criterion: docker compose run --rm toolbox javac -version prints javac 1.8.0_xxx
  ```
- 對應 `main` 分支：[734bfba](https://github.com/titangene/goos-java/commit/734bfba7e4aba50fd36160320b330997d0494183)（部分）

### 這一步做什麼

- 在專案根目錄新增 `toolbox.Dockerfile` 與 `docker-compose.yml`，toolbox 只有 JDK 8
- compose 把專案目錄掛載到 `/app`，container 以 `sleep infinity` 常駐

### 設計理由

- xvfb、X11 函式庫、curl 都還沒加：等測試真的需要 display 與 Openfire 時才加

### 驗證

指令：

```
docker compose run --rm toolbox javac -version
```

#### 完成後的狀態

預期：成功，印出 `javac 1.8.0_xxx`

實際輸出：

```
javac 1.8.0_504
[exit code: 0]
```

---

## 定義：[248189f](https://github.com/titangene/goos-java/commit/248189ffcb532df261380da175d7fabda4dc2782) 新增 stair-step 測試 HelloWorldTest 與 JUnit、Hamcrest jar

- commit message：

  ```text
  test(e2e): define - add a JUnit stair-step test

  Vendors JUnit 4.6 and Hamcrest core 1.2 together with the test, so the
  test compiles and passes on its own. The assertion only proves that the
  toolchain can run a test.

  Test case: helloWorld
  ```
- 對應 `main` 分支：[f02d5df](https://github.com/titangene/goos-java/commit/f02d5df05937b6cbbca0fd48e9d81b8618428784)（部分）、[6dfeed6](https://github.com/titangene/goos-java/commit/6dfeed6a075915fda430f221f20ebd290b23b1c5)（部分）

### 這一步做什麼

- 新增 `test/end-to-end/HelloWorldTest.java`，斷言 `assertEquals("Hello World", "Hello World")`
- 新增 `lib/develop/junit-dep-4.6.jar`、`lib/develop/hamcrest-core-1.2.jar`

### 設計理由

- 斷言只用來證明測試工具鏈可以編譯並執行測試，之後紅燈才不會被誤認成環境問題
- 測試與 jar 在同一個 commit：`CLAUDE.md` 規定 `define` commit 可以帶 jar，所以這個 commit 沒有紅燈，直接通過
- 還沒有測試腳本，驗證指令直接用 `javac` 與 `JUnitCore`

### 驗證

指令：

```
docker compose run --rm toolbox sh -c 'javac -d /tmp -cp "lib/develop/*" test/end-to-end/HelloWorldTest.java && java -cp "/tmp:lib/develop/*" org.junit.runner.JUnitCore HelloWorldTest'
```

#### 完成後的狀態

預期：成功，`OK (1 test)`

實際輸出：

```
JUnit version 4.6
.
Time: 0.002
OK (1 test)
[exit code: 0]
```

---

## infra：[fb6d16b](https://github.com/titangene/goos-java/commit/fb6d16b0e27b35a749e8d1f9a95cd49f30adf8be) 新增 E2E 測試腳本

- commit message：

  ```text
  build(docker): add the end-to-end test script

  Passing check: scripts/test.sh
  ```
- 對應 `main` 分支：[44005f9](https://github.com/titangene/goos-java/commit/44005f972f6e768153875e65f6282e3c523ec89a)（部分）、[c97effa](https://github.com/titangene/goos-java/commit/c97effa80c6b854a8b43d3e38f49ba0d68bed6c9)（部分）

### 這一步做什麼

- 新增 `scripts/test.sh`：啟動 toolbox，在 container 內編譯 `test/end-to-end`，並用 `JUnitCore` 執行所有名稱以 `Test` 結尾的 class
- `.gitignore` 加入 `build-docker/`

### 設計理由

- 編譯輸出放在 `build-docker/`，不放進 git
- 不用重新 build docker image：程式碼是 bind mount 進 `/app` 的，每次都會跑到最新的程式碼
- 測試 class 是從編譯結果中用名稱找出來的，之後新增測試不用改腳本

### 驗證

指令：

```
scripts/test.sh
```

#### 完成後的狀態

預期：成功，`OK (1 test)`

實際輸出：

```
== compiling end-to-end tests ==
== running end-to-end tests ==
JUnit version 4.6
.
Time: 0.002

OK (1 test)

[exit code: 0]
```

---

## infra：[b3fd83f](https://github.com/titangene/goos-java/commit/b3fd83f7fbf36280308c7ff5420f3f516bc460c4) 設定 IntelliJ 與 VS Code 指向 lib/ 目錄

- commit message：

  ```text
  build(ide): point IntelliJ and VS Code at the lib/ directories

  Passing check: grep -q 'lib/develop/\*.jar' .vscode/settings.json && grep -q '"\*\*/\*-src.jar"' .vscode/settings.json && grep -q 'name="lib" level="project"' goos-java.iml && grep -q 'name="develop" level="project"' test/test.iml && grep -q 'module-name="goos-java-baby-step-v2"' test/test.iml
  ```
- 對應 `main` 分支：[6dfeed6](https://github.com/titangene/goos-java/commit/6dfeed6a075915fda430f221f20ebd290b23b1c5)（部分）

### 這一步做什麼

- 新增 VS Code 設定 `.vscode/settings.json`
- 新增 IntelliJ 設定 `goos-java.iml`、`test/test.iml`、`.idea/vcs.xml`、`.idea/.gitignore`

### 設計理由

- 兩個 IDE 都指向 `lib/` 的資料夾層級，之後新增或移除 jar 不用改設定檔
- `.idea/workspace.xml` 是個人設定，由 `.idea/.gitignore` 排除，不進 git
