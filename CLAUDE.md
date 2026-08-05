# goos-java

## Repo 結構：Bare Repo + Git Worktree

本專案採用 bare repo 模式管理（`.bare/` + `.git` 指向 `.bare` + 各分支獨立 worktree 子目錄，如
`main/`），以便多分支平行開發。目前已建立的 worktree：`main/`（本檔案所在位置）。

用法（新增分支 worktree、管理指令、已知限制等）見 `git-worktree-bare-repo` skill，不在此重複。
