echo "optimus-ui"
if [ "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker ps -q -f name=optimus-ui")" ]; then
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-ui"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-ui"

elif [ ! "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker ps -q -f name=optimus-ui")" &&  "$(ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker ps -aq -f status=exited -f name=optimus-ui")" ]; then

        # cleanup
        ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-ui"
    fi
fi
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker kill optimus-ui"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker rm optimus-ui"
cp /tmp/index.html /var/jenkins_home/workspace/Dev.Optimus-UI/dist/.
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker cp 32f2ae7d7dd0:/var/jenkins_home/workspace/Dev.Optimus-UI/dist/. /opt/optimus-ui/public-html/optimus"
#chmod 777 -R /opt/optimus-ui/public-html/optimus
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker build /opt/optimus-ui/ -t optimus-ui"
ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker run -d --name optimus-ui -p 8095:80 -t optimus-ui"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker tag optimus-ui:latest 10.133.208.127:5000/optimus-ui"
#ssh -o StrictHostKeyChecking=no pmurugan@INP44XDAPP2545 -p 5522 "sudo docker push 10.133.208.127:5000/optimus-ui"
rm -rf /var/jenkins_home/jobs/Dev.Optimus-UI/modules/*

