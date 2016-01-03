<?php
defined('BASEPATH') OR exit('No direct script access allowed');
$route['position']='Login/position';
$route['Search_ID']='Login/Search_ID';

$route['uploading']='Login/uploading';
$route['upload']='Login/upload';

$route['User_Info']='Login/User_info';
$route['User_Photo'] ='Login/User_photo';
$route['seed']='Login/seed';
$route['getChatroom']='Login/getChatroom';
$route['blockadd']='Login/blockadd';
$route['blockdelete']='Login/blockdelete';
$route['blocklist']='Login/blocklist';
$route['rating']='Login/rating';

$route['waitChat']='Login/waitChat';

$route['applyChat']='Login/applyChat';

$route['SearchChat'] ='Login/SearchChat';
$route['VerifyChat'] ='Login/VerifyChat';

$route['chatamount']='Login/userChatroomQuery';
$route['createchat']='Login/createchat';
$route['register']='Login/register';
$route['getmember']='Login/getMember';
$route['delete/(:num)']='Login/delete/$1';
$route['Search_User']='Login/edit';
$route['editact']='Login/editact';
$route['success']='Login/success';
$route['login']='Login/login';
$route['verify']='Login/verify';
$route['room']='Login/room';
$route['table']='Login/table';
$route['default_controller'] = 'welcome';
$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;


$route['member_web']='Login/edit_web';
