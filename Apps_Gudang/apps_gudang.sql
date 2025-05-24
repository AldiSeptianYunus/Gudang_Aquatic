-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 24, 2025 at 05:33 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `apps_gudang`
--

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `id_barang` varchar(10) NOT NULL,
  `nama_barang` varchar(150) NOT NULL,
  `jenis` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`id_barang`, `nama_barang`, `jenis`) VALUES
('12', 'Aqua Mineral', 'Minuman'),
('98', 'Granita Anget', 'Minuman');

-- --------------------------------------------------------

--
-- Table structure for table `barang_keluar`
--

CREATE TABLE `barang_keluar` (
  `id_keluar` varchar(20) NOT NULL,
  `no_faktur` varchar(20) NOT NULL,
  `tanggal_keluar` date NOT NULL,
  `id_toko` varchar(10) NOT NULL,
  `id_mobil` varchar(10) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `barang_keluar`
--

INSERT INTO `barang_keluar` (`id_keluar`, `no_faktur`, `tanggal_keluar`, `id_toko`, `id_mobil`, `id_user`, `created_at`) VALUES
('BK0001', 'BK02', '2025-05-24', '8989', '54', 999, '2025-05-24 10:50:36'),
('BK0002', 'BK0999', '2025-05-24', '8989', '54', 999, '2025-05-24 11:49:58'),
('BK0003', 'BK56', '2025-05-24', '8989', '54', 999, '2025-05-24 12:29:32');

-- --------------------------------------------------------

--
-- Table structure for table `barang_masuk`
--

CREATE TABLE `barang_masuk` (
  `id_masuk` varchar(20) NOT NULL,
  `no_faktur` varchar(20) NOT NULL,
  `tanggal_masuk` date NOT NULL,
  `id_supplier` varchar(20) NOT NULL,
  `id_mobil` varchar(20) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `barang_masuk`
--

INSERT INTO `barang_masuk` (`id_masuk`, `no_faktur`, `tanggal_masuk`, `id_supplier`, `id_mobil`, `id_user`, `created_at`) VALUES
('BM0001', 'BM12', '2025-05-24', '5656', '54', 999, '2025-05-24 10:48:39'),
('BM0002', 'BM123', '2025-05-24', '5656', '54', 999, '2025-05-24 11:49:02'),
('BM0003', 'BM8888', '2025-05-24', '5656', '54', 999, '2025-05-24 12:28:27');

-- --------------------------------------------------------

--
-- Table structure for table `detail_barang_keluar`
--

CREATE TABLE `detail_barang_keluar` (
  `id_detail_keluar` int(11) NOT NULL,
  `id_keluar` varchar(10) NOT NULL,
  `id_barang` varchar(10) NOT NULL,
  `jumlah` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_barang_keluar`
--

INSERT INTO `detail_barang_keluar` (`id_detail_keluar`, `id_keluar`, `id_barang`, `jumlah`) VALUES
(3, 'BK0001', '12', 15),
(4, 'BK0002', '12', 5),
(5, 'BK0003', '12', 20);

-- --------------------------------------------------------

--
-- Table structure for table `detail_barang_masuk`
--

CREATE TABLE `detail_barang_masuk` (
  `id_detail_masuk` int(11) NOT NULL,
  `id_masuk` varchar(20) NOT NULL,
  `id_barang` varchar(10) NOT NULL,
  `jumlah` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `detail_barang_masuk`
--

INSERT INTO `detail_barang_masuk` (`id_detail_masuk`, `id_masuk`, `id_barang`, `jumlah`) VALUES
(4, 'BM0001', '12', 40),
(5, 'BM0002', '98', 90),
(6, 'BM0003', '12', 50);

-- --------------------------------------------------------

--
-- Table structure for table `mobil`
--

CREATE TABLE `mobil` (
  `id_mobil` varchar(20) NOT NULL,
  `plat_mobil` varchar(50) NOT NULL,
  `nama_driver` varchar(100) NOT NULL,
  `jenis` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mobil`
--

INSERT INTO `mobil` (`id_mobil`, `plat_mobil`, `nama_driver`, `jenis`) VALUES
('54', 'B4501SPK', 'Aldi', 'Truck');

-- --------------------------------------------------------

--
-- Table structure for table `stok_barang`
--

CREATE TABLE `stok_barang` (
  `id_stok` int(11) NOT NULL,
  `id_barang` varchar(10) NOT NULL,
  `jumlah` int(11) DEFAULT '0',
  `tanggal_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stok_barang`
--

INSERT INTO `stok_barang` (`id_stok`, `id_barang`, `jumlah`, `tanggal_update`) VALUES
(4, '12', 45, '2025-05-24 12:31:13'),
(5, '98', 90, '2025-05-24 11:49:02');

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `id_supplier` varchar(10) NOT NULL,
  `nama_supplier` varchar(100) NOT NULL,
  `alamat` text,
  `telepon` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`id_supplier`, `nama_supplier`, `alamat`, `telepon`) VALUES
('5656', 'Danon Minum', 'Danon1', '123456');

-- --------------------------------------------------------

--
-- Table structure for table `toko`
--

CREATE TABLE `toko` (
  `id_toko` varchar(10) NOT NULL,
  `nama_toko` varchar(100) NOT NULL,
  `alamat` text,
  `telepon` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `toko`
--

INSERT INTO `toko` (`id_toko`, `nama_toko`, `alamat`, `telepon`) VALUES
('8989', 'Alfamidi', 'Alfa 1', '102938');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int(10) NOT NULL,
  `nama_user` varchar(150) NOT NULL,
  `password` varchar(150) NOT NULL,
  `tggl_lahir` date NOT NULL,
  `jenis_kelamin` varchar(50) NOT NULL,
  `alamat` varchar(250) NOT NULL,
  `level` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `nama_user`, `password`, `tggl_lahir`, `jenis_kelamin`, `alamat`, `level`) VALUES
(1, 'aldi', 'aldi123', '2025-05-24', 'Pria', 'X6G Aldi Septian Yunus', 'Staff'),
(999, 'admin', 'admin123', '2025-05-24', 'Pria', 'X6G Kelompok 2', 'Administrator');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indexes for table `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD PRIMARY KEY (`id_keluar`),
  ADD UNIQUE KEY `no_faktur` (`no_faktur`),
  ADD KEY `id_toko` (`id_toko`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD PRIMARY KEY (`id_masuk`),
  ADD UNIQUE KEY `no_faktur` (`no_faktur`),
  ADD KEY `id_supplier` (`id_supplier`),
  ADD KEY `id_mobil` (`id_mobil`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `detail_barang_keluar`
--
ALTER TABLE `detail_barang_keluar`
  ADD PRIMARY KEY (`id_detail_keluar`),
  ADD KEY `id_keluar` (`id_keluar`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indexes for table `detail_barang_masuk`
--
ALTER TABLE `detail_barang_masuk`
  ADD PRIMARY KEY (`id_detail_masuk`),
  ADD KEY `id_masuk` (`id_masuk`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indexes for table `mobil`
--
ALTER TABLE `mobil`
  ADD PRIMARY KEY (`id_mobil`);

--
-- Indexes for table `stok_barang`
--
ALTER TABLE `stok_barang`
  ADD PRIMARY KEY (`id_stok`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`id_supplier`);

--
-- Indexes for table `toko`
--
ALTER TABLE `toko`
  ADD PRIMARY KEY (`id_toko`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detail_barang_keluar`
--
ALTER TABLE `detail_barang_keluar`
  MODIFY `id_detail_keluar` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `detail_barang_masuk`
--
ALTER TABLE `detail_barang_masuk`
  MODIFY `id_detail_masuk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `stok_barang`
--
ALTER TABLE `stok_barang`
  MODIFY `id_stok` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD CONSTRAINT `barang_keluar_ibfk_1` FOREIGN KEY (`id_toko`) REFERENCES `toko` (`id_toko`) ON UPDATE CASCADE,
  ADD CONSTRAINT `barang_keluar_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD CONSTRAINT `barang_masuk_ibfk_1` FOREIGN KEY (`id_supplier`) REFERENCES `supplier` (`id_supplier`),
  ADD CONSTRAINT `barang_masuk_ibfk_2` FOREIGN KEY (`id_mobil`) REFERENCES `mobil` (`id_mobil`),
  ADD CONSTRAINT `barang_masuk_ibfk_3` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

--
-- Constraints for table `detail_barang_keluar`
--
ALTER TABLE `detail_barang_keluar`
  ADD CONSTRAINT `detail_barang_keluar_ibfk_1` FOREIGN KEY (`id_keluar`) REFERENCES `barang_keluar` (`id_keluar`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_barang_keluar_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`) ON UPDATE CASCADE;

--
-- Constraints for table `detail_barang_masuk`
--
ALTER TABLE `detail_barang_masuk`
  ADD CONSTRAINT `detail_barang_masuk_ibfk_1` FOREIGN KEY (`id_masuk`) REFERENCES `barang_masuk` (`id_masuk`),
  ADD CONSTRAINT `detail_barang_masuk_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`);

--
-- Constraints for table `stok_barang`
--
ALTER TABLE `stok_barang`
  ADD CONSTRAINT `stok_barang_ibfk_1` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
