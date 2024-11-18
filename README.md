# Distributed System
Đây là đồ án môn học do thầy Bảo dạy cũng như hướng dẫn. Nhóm chọn đề tài số 9, 
đề tài này được làm theo nhóm gồm 5 người.
## Project Name
Đề tài gồm 1-2 máy Client, 1 máy Coordinator và 2 máy Server. Hai máy Server sẽ chứa 2 database giống nhau
Nhiệm vụ: Khi máy Client gửi request đến Server phải thông qua máy điều phối Coordinator, client gửi request đến Server nào thì máy 
Coordiantor nhận request và gửi request đến Server đó. Server nhận request từ Coordinator, sau đó lấy dữ liệu từ db gửi ngược lại máy Coordinator 
và máy Coordinator gửi response về máy client đã gửi request đi.
### Explain Project
Giải thích: Ta có 2 database giống nhau quản lý user. Server 1 sẽ kết nối đến database cơ sở 1, Server 2 kết nối đến database cơ sở 2.
Máy Client sẽ gửi request đến cơ sở 1 ví dụ (lấy danh sách user ở cơ sở 1). Request sẽ được nhận bởi Coordinator và biết được địa chỉ ip máy client nào gửi
và biết được client gửi đến cơ sở 1
