package com.source3g.hermes.sync.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

/**
 * TARGZIP工具
 * 
 * @author 肇斌
 * @since 1.0
 */
public abstract class TarGZipUtils {

	private static final String BASE_DIR = "";

	// 符号"/"用来作为目录标识判断符
	private static final String PATH = "/";
	private static final int BUFFER = 1024;

	private static final String EXT = ".tar";

	/**
	 * 归档
	 * 
	 * @param srcPath
	 * @param destPath
	 * @throws Exception
	 */
	public static void archive(String srcPath, String destPath) throws Exception {

		File srcFile = new File(srcPath);

		archive(srcFile, destPath);

	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 *            源路径
	 * @param destPath
	 *            目标路径
	 * @throws Exception
	 */
	public static void archive(File srcFile, File destFile) throws Exception {

		TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(destFile));

		archive(srcFile, taos, BASE_DIR);

		taos.flush();
		taos.close();
	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 * @throws Exception
	 */
	public static void archive(File srcFile) throws Exception {
		String name = srcFile.getName();
		String basePath = srcFile.getPath();
		String destPath = basePath + name + EXT;
		archive(srcFile, destPath);
	}

	/**
	 * 归档文件
	 * 
	 * @param srcFile
	 * @param destPath
	 * @throws Exception
	 */
	public static void archive(File srcFile, String destPath) throws Exception {
		archive(srcFile, new File(destPath));
	}

	/**
	 * 归档
	 * 
	 * @param srcPath
	 * @throws Exception
	 */
	public static void archive(String srcPath) throws Exception {
		File srcFile = new File(srcPath);

		archive(srcFile);
	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 *            源路径
	 * @param taos
	 *            TarArchiveOutputStream
	 * @param basePath
	 *            归档包内相对路径
	 * @throws Exception
	 */
	private static void archive(File srcFile, TarArchiveOutputStream taos, String basePath) throws Exception {
		if (srcFile.isDirectory()) {
			archiveDir(srcFile, taos, basePath);
		} else {
			archiveFile(srcFile, taos, basePath);
		}
	}

	/**
	 * 目录归档
	 * 
	 * @param dir
	 * @param taos
	 *            TarArchiveOutputStream
	 * @param basePath
	 * @throws Exception
	 */
	private static void archiveDir(File dir, TarArchiveOutputStream taos, String basePath) throws Exception {

		File[] files = dir.listFiles();

		if (files.length < 1) {
			TarArchiveEntry entry = new TarArchiveEntry(basePath + dir.getName() + PATH);

			taos.putArchiveEntry(entry);
			taos.closeArchiveEntry();
		}

		for (File file : files) {

			// 递归归档
			archive(file, taos, basePath + dir.getName() + PATH);

		}
	}

	/**
	 * 数据归档
	 * 
	 * @param data
	 *            待归档数据
	 * @param path
	 *            归档数据的当前路径
	 * @param name
	 *            归档文件名
	 * @param taos
	 *            TarArchiveOutputStream
	 * @throws Exception
	 */
	private static void archiveFile(File file, TarArchiveOutputStream taos, String dir) throws Exception {

		/**
		 * 归档内文件名定义
		 * 
		 * <pre>
		 * 如果有多级目录，那么这里就需要给出包含目录的文件名 
		 * 如果用WinRAR打开归档包，中文名将显示为乱码
		 * </pre>
		 */
		TarArchiveEntry entry = new TarArchiveEntry(dir + file.getName());

		entry.setSize(file.length());

		taos.putArchiveEntry(entry);

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = bis.read(data, 0, BUFFER)) != -1) {
			taos.write(data, 0, count);
		}

		bis.close();

		taos.closeArchiveEntry();
	}

	/**
	 * 解归档
	 * 
	 * @param srcFile
	 * @throws Exception
	 */
	public static void dearchive(File srcFile) throws Exception {
		String basePath = srcFile.getParent();
		dearchive(srcFile, basePath);
	}

	/**
	 * 解归档
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static void dearchive(File srcFile, File destFile) throws Exception {

		TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(srcFile));
		dearchive(destFile, tais);

		tais.close();

	}

	/**
	 * 解归档
	 * 
	 * @param srcFile
	 * @param destPath
	 * @throws Exception
	 */
	public static void dearchive(File srcFile, String destPath) throws Exception {
		dearchive(srcFile, new File(destPath));

	}

	/**
	 * 文件 解归档
	 * 
	 * @param destFile
	 *            目标文件
	 * @param tais
	 *            ZipInputStream
	 * @throws Exception
	 */
	private static void dearchive(File destFile, TarArchiveInputStream tais) throws Exception {

		TarArchiveEntry entry = null;
		while ((entry = tais.getNextTarEntry()) != null) {

			// 文件
			String dir = destFile.getPath() + File.separator + entry.getName();

			File dirFile = new File(dir);

			// 文件检查
			fileProber(dirFile);

			if (entry.isDirectory()) {
				dirFile.mkdirs();
			} else {
				dearchiveFile(dirFile, tais);
			}

		}
	}

	/**
	 * 文件 解归档
	 * 
	 * @param srcPath
	 *            源文件路径
	 * 
	 * @throws Exception
	 */
	public static void dearchive(String srcPath) throws Exception {
		File srcFile = new File(srcPath);

		dearchive(srcFile);
	}

	/**
	 * 文件 解归档
	 * 
	 * @param srcPath
	 *            源文件路径
	 * @param destPath
	 *            目标文件路径
	 * @throws Exception
	 */
	public static void dearchive(String srcPath, String destPath) throws Exception {

		File srcFile = new File(srcPath);
		dearchive(srcFile, destPath);
	}

	/**
	 * 文件解归档
	 * 
	 * @param destFile
	 *            目标文件
	 * @param tais
	 *            TarArchiveInputStream
	 * @throws Exception
	 */
	private static void dearchiveFile(File destFile, TarArchiveInputStream tais) throws Exception {

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = tais.read(data, 0, BUFFER)) != -1) {
			bos.write(data, 0, count);
		}

		bos.close();
	}

	/**
	 * 文件探针
	 * 
	 * <pre>
	 * 当父目录不存在时，创建目录！
	 * </pre>
	 * 
	 * @param dirFile
	 */
	private static void fileProber(File dirFile) {

		File parentFile = dirFile.getParentFile();
		if (!parentFile.exists()) {

			// 递归寻找上级目录
			fileProber(parentFile);

			parentFile.mkdir();
		}
	}

	/**
	 * 
	 * @Title: compress
	 * @Description: 将文件用gzip压缩
	 * @param source
	 *            需要压缩的文件
	 * @return File 返回压缩后的文件
	 * @throws
	 */
	public static File gzip(File source) {
		File target = new File(source.getAbsolutePath() + ".gz");
		FileInputStream in = null;
		GZIPOutputStream out = null;
		try {
			in = new FileInputStream(source);
			out = new GZIPOutputStream(new FileOutputStream(target));
			byte[] array = new byte[1024];
			int number = -1;
			while ((number = in.read(array, 0, array.length)) != -1) {
				out.write(array, 0, number);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return target;
	}

	public static File tarGzip(String folder) throws Exception {
		File srcFolder = new File(folder);
		// String name = srcFolder.getName();
		String basePath = srcFolder.getAbsolutePath();
		String destPath = basePath + EXT;
		File destFile = new File(destPath);
		archive(new File(folder), destFile);
		if (destFile.exists()) {
			return gzip(destFile);
		}
		return null;
	}

	/**
	 * 将指定的某些文件打包
	 * 
	 * @param baseDir
	 *            基本目录
	 * @param files
	 * @param destPath
	 * @return
	 * @throws Exception
	 */
	public static File tarGzip(String baseDir, File[] files, String destPath) throws Exception {
		destPath += EXT;
		File destFile = new File(destPath);
		if (files != null && files.length > 0) {
			TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(destFile));
			for (File f : files) {
				archive(f, taos, baseDir);
			}
			taos.flush();
			taos.close();
			if (destFile.exists()) {
				File gzipFile = gzip(destFile);
				destFile.delete();
				return gzipFile;
			}
		}
		return null;
	}

	public static File unGzip(String filePath, String destFilePath) throws IOException {
		FileInputStream fin = new FileInputStream(new File(filePath));
		GZIPInputStream gzip = new GZIPInputStream(fin);
		byte[] buf = new byte[1024];
		int num = -1;
		FileOutputStream out = new FileOutputStream(new File(destFilePath));
		while ((num = gzip.read(buf, 0, buf.length)) != -1) {
			out.write(buf, 0, num);
		}
		out.flush();
		out.close();
		gzip.close();
		fin.close();
		return new File(destFilePath);
	}

	public static File unTarGzip(String filePath, String destFilePath) throws Exception {
		String tarDestFilePath = destFilePath + EXT;
		File tarDestFile = unGzip(filePath, tarDestFilePath);
		dearchive(tarDestFile, destFilePath);
		File destFile = new File(destFilePath);
		if (destFile.exists()) {
			return destFile;
		}
		return null;
	}

}