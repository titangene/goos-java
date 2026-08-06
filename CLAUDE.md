# goos-java

## Repo 結構：Bare Repo + Git Worktree

本專案採用 bare repo 模式管理（`.bare/` + `.git` 指向 `.bare` + 各分支獨立 worktree 子目錄，如
`main/`），以便多分支平行開發。目前已建立的 worktree：`main/`（本檔案所在位置）。

用法（新增分支 worktree、管理指令、已知限制等）見 `git-worktree-bare-repo` skill，不在此重複。

## Java 一律用 Docker 執行

本專案的 Java（編譯、執行、測試）一律透過 `docker/docker-compose.yml` 定義的 `toolbox` container 執行，不在 host 直接跑 `javac`/`java`。

跑 end-to-end 測試：`docker/scripts/test.sh`（會啟動 `toolbox` container，並在其中執行 `docker/scripts/run-e2e-tests.sh`）。
