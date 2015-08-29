$1 == "commit" {commit = $2}
$1 == "A" {printf "%s;ADDED; ;%s\n", commit, $2 }
$1 == "M" {printf "%s;MODIFIED; ;%s\n", commit, $2 } 
match($1,"R[0-9][0-9][0-9]") {printf "%s;RENAMED;%s;%s\n", commit, $2, $3 } 

