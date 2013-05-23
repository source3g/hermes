#! /system/bin/sh
rm -rf /mnt/sdcard/Wangcaibao/download/menu_list/
mkdir /mnt/sdcard/Wangcaibao/download/menu_list/
mv $1/list.json /mnt/sdcard/Wangcaibao/download/menu_list/
mv $1/img/ /mnt/sdcard/Wangcaibao/download/menu_list/img/
echo success