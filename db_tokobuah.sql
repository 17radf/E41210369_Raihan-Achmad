-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 03, 2021 at 04:59 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_tokobuah`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_bagianrole`
--

CREATE TABLE `tb_bagianrole` (
  `id` int(11) NOT NULL,
  `nama` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_bagianrole`
--

INSERT INTO `tb_bagianrole` (`id`, `nama`) VALUES
(1, 'Checking'),
(2, 'Stock');

-- --------------------------------------------------------

--
-- Table structure for table `tb_buah`
--

CREATE TABLE `tb_buah` (
  `id` int(11) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `stok` double NOT NULL,
  `harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_buah`
--

INSERT INTO `tb_buah` (`id`, `nama`, `stok`, `harga`) VALUES
(1, 'Apel', 0, 4000),
(2, 'Jeruk', 8, 5000),
(3, 'Pisang', 5, 10000),
(4, 'Apel Fuji', 23, 15000),
(5, 'Pisang King Kong', 0, 1000000),
(8, 'Eden', 1, 10000),
(9, 'Asem Monyet', 14, 3000),
(10, 'Naga', 20, 5000),
(11, 'Nagatus', 20, 5000),
(12, 'Buah Kuldi', 19, 5000),
(13, 'Nangka', 20, 15000),
(14, 'Semangka', 20, 5000),
(15, 'Apricot', 19, 5000),
(16, 'Avocado', 19, 5000),
(17, 'Banana', 19, 5000),
(18, 'Blueberry', 19, 5000),
(19, 'Cherry', 19, 5000),
(20, 'Cucumber', 19, 5000),
(21, 'Crab Apple', 19, 5000),
(22, 'Cantaloupe', 19, 5000),
(23, 'Boysenberry', 19, 5000),
(24, 'Damson plum', 19, 3000),
(25, 'Pluots', 20, 5000),
(26, 'Dates', 19, 5000),
(27, 'Dewberries', 19, 5000),
(28, 'Dragon Fruit', 19, 5000),
(29, 'Elderberry', 19, 5000),
(30, 'Fig', 19, 5000),
(31, 'Eggfruit', 19, 5000),
(32, 'Evergreen Huckleberry', 19, 5000),
(33, 'Entawak', 20, 5000),
(34, 'Finger Lime', 19, 5000),
(35, 'Grapefruit', 19, 5000),
(36, 'Grape', 20, 5000),
(37, 'Gooseberry', 19, 5000),
(38, 'Guava', 19, 5000),
(39, 'Honeydew Melon', 19, 5000),
(40, 'Plum', 20, 5000),
(41, 'Java Apple', 19, 5000),
(42, 'Lime', 19, 5000),
(43, 'Kiwi', 19, 5000),
(44, 'Mango', 20, 5000),
(45, 'Kumquat', 19, 5000),
(46, 'Lychee', 19, 5000),
(47, 'Melon', 20, 5000),
(48, 'Mango', 15, 5000),
(49, 'Mulberry', 20, 5000);

-- --------------------------------------------------------

--
-- Table structure for table `tb_detail_transaksi`
--

CREATE TABLE `tb_detail_transaksi` (
  `id` int(11) NOT NULL,
  `id_transaksi` int(11) NOT NULL,
  `id_buah` int(11) NOT NULL,
  `qty` int(3) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_detail_transaksi`
--

INSERT INTO `tb_detail_transaksi` (`id`, `id_transaksi`, `id_buah`, `qty`, `id_user`) VALUES
(8, 7, 2, 3, 2),
(11, 7, 5, 1, 2),
(12, 7, 3, 2, 2),
(20, 12, 5, 1, 2),
(27, 10, 4, 1, 2),
(30, 10, 3, 3, 2),
(31, 10, 4, 2, 2),
(32, 10, 5, 1, 2),
(34, 10, 1, 1, 2),
(36, 9, 1, 5, 2),
(37, 9, 1, 15, 2),
(38, 9, 5, 2, 2),
(39, 9, 3, 30, 2),
(40, 11, 2, 3, 2),
(41, 11, 5, 2, 2),
(42, 13, 4, 1, 2),
(43, 15, 9, 2, 2),
(44, 14, 2, 20, 2),
(45, 17, 3, 2, 2),
(46, 16, 9, 1, 2),
(48, 16, 9, 1, 2),
(54, 16, 9, 1, 2),
(55, 18, 4, 2, 2),
(56, 19, 3, 2, 2),
(57, 20, 2, 9, 2),
(58, 21, 2, 2, 2),
(59, 22, 2, 3, 2),
(60, 23, 3, 3, 2),
(61, 14, 2, 3, 2),
(62, 8, 3, 5, 2),
(63, 17, 48, 5, 2),
(64, 17, 2, 1, 2),
(65, 17, 43, 1, 2),
(66, 17, 45, 1, 2),
(67, 17, 42, 1, 2),
(68, 17, 46, 1, 2),
(69, 17, 4, 1, 2),
(70, 17, 15, 1, 2),
(71, 17, 9, 1, 2),
(72, 17, 16, 1, 2),
(73, 17, 17, 1, 2),
(74, 17, 18, 1, 2),
(75, 17, 23, 1, 2),
(76, 17, 12, 1, 2),
(77, 17, 22, 1, 2),
(78, 17, 19, 1, 2),
(79, 17, 21, 1, 2),
(80, 17, 20, 1, 2),
(81, 17, 24, 1, 2),
(82, 17, 26, 1, 2),
(83, 17, 27, 1, 2),
(84, 17, 28, 1, 2),
(85, 17, 8, 1, 2),
(86, 17, 31, 1, 2),
(87, 17, 29, 1, 2),
(88, 17, 32, 1, 2),
(89, 17, 30, 1, 2),
(90, 17, 34, 1, 2),
(91, 17, 37, 1, 2),
(92, 17, 35, 1, 2),
(93, 17, 38, 1, 2),
(94, 17, 39, 1, 2),
(95, 17, 41, 1, 2),
(96, 17, 2, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `tb_karyawan`
--

CREATE TABLE `tb_karyawan` (
  `id` int(11) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `id_bagian` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_karyawan`
--

INSERT INTO `tb_karyawan` (`id`, `nama`, `id_bagian`) VALUES
(1, 'Supriyadi', 1),
(2, 'Zayn Malik', 2),
(3, 'Raihan', 2),
(5, 'Sokap', 1),
(6, 'Samsul', 2),
(7, 'Whomstve', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tb_pelanggan`
--

CREATE TABLE `tb_pelanggan` (
  `id` int(11) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `alamat` varchar(30) NOT NULL,
  `no_hp` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_pelanggan`
--

INSERT INTO `tb_pelanggan` (`id`, `nama`, `alamat`, `no_hp`) VALUES
(2, 'Zayn Malik', 'Tamansari', '08kepobgt2'),
(3, 'Suheri Styles', 'Kembang', '7776969'),
(4, 'Gandi Eleq', 'Corahdemiu', '08696969'),
(5, 'Jonathan R', 'Lur MLG', '87775423'),
(7, 'Jonathan A', 'Los East', '877759'),
(8, 'Jonathan G', 'Los North', '877759'),
(19, 'Raihan', 'Bro IDK', 'WHAT'),
(20, 'Abang Tukang Bakso', 'Mari Mari Sini', '4Y90393'),
(21, 'Swagger', 'NYC', '4Y90393'),
(22, 'EDEN', 'Vertigo', '082123903');

-- --------------------------------------------------------

--
-- Table structure for table `tb_pembayaran`
--

CREATE TABLE `tb_pembayaran` (
  `id` int(11) NOT NULL,
  `id_transaksi` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `tgl_bayar` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_pembayaran`
--

INSERT INTO `tb_pembayaran` (`id`, `id_transaksi`, `total`, `tgl_bayar`) VALUES
(2, 7, 1035000, '2021-12-01'),
(3, 10, 1079000, '2021-12-01'),
(4, 11, 2015000, '2021-12-01'),
(5, 13, 15000, '2021-12-01'),
(6, 19, 20000, '2021-12-01'),
(7, 20, 45000, '2021-12-02'),
(8, 12, 1000000, '2021-12-02'),
(9, 18, 30000, '2021-12-02'),
(10, 8, 50000, '2021-12-03'),
(11, 14, 115000, '2021-12-03'),
(12, 15, 6000, '2021-12-03'),
(13, 16, 9000, '2021-12-03'),
(15, 17, 223000, '2021-12-03'),
(16, 21, 10000, '2021-12-03');

-- --------------------------------------------------------

--
-- Table structure for table `tb_transaksi`
--

CREATE TABLE `tb_transaksi` (
  `no_faktur` int(11) NOT NULL,
  `id_pelanggan` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `bayar` enum('belum','sudah') NOT NULL DEFAULT 'belum'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_transaksi`
--

INSERT INTO `tb_transaksi` (`no_faktur`, `id_pelanggan`, `tanggal`, `bayar`) VALUES
(7, 3, '2021-11-03', 'sudah'),
(8, 3, '2021-11-24', 'sudah'),
(9, 3, '2021-11-24', 'sudah'),
(10, 2, '2021-11-24', 'sudah'),
(11, 3, '2021-11-26', 'sudah'),
(12, 4, '2021-11-27', 'sudah'),
(13, 3, '2021-12-01', 'sudah'),
(14, 2, '2021-12-01', 'sudah'),
(15, 2, '2021-12-01', 'sudah'),
(16, 7, '2021-12-01', 'sudah'),
(17, 4, '2021-12-01', 'sudah'),
(18, 5, '2021-12-01', 'sudah'),
(19, 8, '2021-12-01', 'sudah'),
(20, 5, '2021-12-01', 'sudah'),
(21, 5, '2021-12-01', 'sudah'),
(22, 19, '2021-12-01', 'belum'),
(23, 4, '2021-12-01', 'belum'),
(24, 8, '2021-12-03', 'belum'),
(25, 20, '2021-12-03', 'belum');

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL,
  `username` varchar(15) NOT NULL,
  `password` varchar(15) NOT NULL,
  `nama` varchar(30) DEFAULT NULL,
  `email` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`id`, `username`, `password`, `nama`, `email`) VALUES
(1, 'test', '123', 'WWGombel', 'gombel@gmail.com'),
(2, 'raihan', 'newvadas', 'Raihan A', 'yagitulah@gmail.com'),
(6, 'maaf', 'wow', 'karyawan', '123'),
(7, 'suheri', '123', 'Suheri', 'styles@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_bagianrole`
--
ALTER TABLE `tb_bagianrole`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_buah`
--
ALTER TABLE `tb_buah`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_transaksi` (`id_transaksi`),
  ADD KEY `id_buah` (`id_buah`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `tb_karyawan`
--
ALTER TABLE `tb_karyawan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_bagian` (`id_bagian`);

--
-- Indexes for table `tb_pelanggan`
--
ALTER TABLE `tb_pelanggan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_pembayaran`
--
ALTER TABLE `tb_pembayaran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_transaksi` (`id_transaksi`);

--
-- Indexes for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD PRIMARY KEY (`no_faktur`),
  ADD KEY `id_pelanggan` (`id_pelanggan`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_bagianrole`
--
ALTER TABLE `tb_bagianrole`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tb_buah`
--
ALTER TABLE `tb_buah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97;

--
-- AUTO_INCREMENT for table `tb_karyawan`
--
ALTER TABLE `tb_karyawan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `tb_pelanggan`
--
ALTER TABLE `tb_pelanggan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `tb_pembayaran`
--
ALTER TABLE `tb_pembayaran`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  MODIFY `no_faktur` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `tb_user`
--
ALTER TABLE `tb_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  ADD CONSTRAINT `tb_detail_transaksi_ibfk_1` FOREIGN KEY (`id_transaksi`) REFERENCES `tb_transaksi` (`no_faktur`),
  ADD CONSTRAINT `tb_detail_transaksi_ibfk_2` FOREIGN KEY (`id_buah`) REFERENCES `tb_buah` (`id`),
  ADD CONSTRAINT `tb_detail_transaksi_ibfk_3` FOREIGN KEY (`id_user`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_karyawan`
--
ALTER TABLE `tb_karyawan`
  ADD CONSTRAINT `tb_karyawan_ibfk_1` FOREIGN KEY (`id_bagian`) REFERENCES `tb_bagianrole` (`id`);

--
-- Constraints for table `tb_pembayaran`
--
ALTER TABLE `tb_pembayaran`
  ADD CONSTRAINT `tb_pembayaran_ibfk_1` FOREIGN KEY (`id_transaksi`) REFERENCES `tb_transaksi` (`no_faktur`);

--
-- Constraints for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD CONSTRAINT `tb_transaksi_ibfk_1` FOREIGN KEY (`id_pelanggan`) REFERENCES `tb_pelanggan` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
