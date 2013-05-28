#! /system/bin/sh
busybox rm -rf /mnt/sdcard/Wangcaibao/download/menu_list/
busybox mkdir /mnt/sdcard/Wangcaibao/download/menu_list/
busybox mv $1/list.json /mnt/sdcard/Wangcaibao/download/menu_list/
busybox mv $1/img/ /mnt/sdcard/Wangcaibao/download/menu_list/img/
echo success