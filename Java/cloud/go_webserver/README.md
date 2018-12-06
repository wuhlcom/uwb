## Compile webserver

### linux
`go build -o webserver webserver.go`

### Mac
`GOOS=darwin GOARCH=amd64 go build -o webserver.darwin-amd64 webserver.go`

192.168.10.232:8000/hello.txt