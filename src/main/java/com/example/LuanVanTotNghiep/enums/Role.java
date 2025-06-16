package com.example.LuanVanTotNghiep.enums;

//Phân quyền của người dùng trong hệ thống
// ADMIN: Quản trị viên, có quyền cao nhất
// CADRE: Cán bộ, có quyền quản lý hồ sơ và người dùng
// STUDENT: Sinh viên, có quyền tạo và xem hồ sơ của mình
public enum Role {
    ADMIN,
    CADRE,
    STUDENT,

}
