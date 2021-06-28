#export project=wschart

#echo "project:${project}"

#cd /data/docker-swarm/${project}

git remote set-url origin https://ctc948040:taechul9**@github.com/ctc94/download.git

git add .

git status

echo "date:$(date "+%Y-%m-%d %_H:%M:%S")"

git commit -a -m "$(date "+%Y-%m-%d %_H:%M:%S") modify files"

git status

git push origin main
