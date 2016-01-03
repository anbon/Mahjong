<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Test extends CI_Controller {
	public function __construct(){
    	parent::__construct();
    	$this->load->model('Pmodel');
    }


	public function add(){
		$count=$this->input->post('count');

		$title=$this->input->post('title');
		$main=$this->Pmodel->addmax();
		
		for($i=1;$i<=$count;$i++){
			$mintitle=$this->input->post('mintitle'.$i);
			$progress=$this->input->post('progress'.$i);
			$date=$this->input->post('date'.$i);
			$date=substr($date,6,4).'/'.substr($date,0,5);
			if($mintitle==''||$progress==''||$date==''){
				$count-=1;
			}
			else{
				$this->Pmodel->addmin($mintitle,$progress,$date,$main);
			}
		}

		$this->Pmodel->addtitle($main,$title,$count);
		echo "<script>location.href='".base_url("index.php/table")."'; </script>";


	}
	public function home()
	{
		$this->load->view('test/forms');
	}
	public function table()
	{	
		$ar=$this->Pmodel->get_table();
		$data['ar']=$ar;
		$br=$this->Pmodel->get_min();
		$data['br']=$br;
		$this->load->view('test/tables',$data);
	}
	public function edit($num){
		$ar=$this->Pmodel->get_edit($num);
		$data['ar']=$ar;
		$br=$this->Pmodel->get_editmin($num);
		$data['br']=$br;
		$this->load->view('test/edit',$data);
	}
	public function editact(){
		$count=$this->input->post('count');
		$num=$this->input->post('num');
		$title=$this->input->post('title');
		
		for($i=1;$i<=$count;$i++){
			$minnum=$this->input->post('minnum'.$i);
			$mintitle=$this->input->post('mintitle'.$i);
			$progress=$this->input->post('progress'.$i);
			$date=$this->input->post('date'.$i);
			$date=substr($date,6,4).'/'.substr($date,0,5);
			if($mintitle==''||$progress==''||$date==''){
				$count-=1;
			}
			else{
				$this->Pmodel->editmin($minnum,$mintitle,$progress,$date);
			}
		}

		$this->Pmodel->edittitle($num,$title,$count);
		echo "<script>location.href='".base_url("index.php/table")."'; </script>";


	}
}
