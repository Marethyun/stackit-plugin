<?php

/**
* @author Marethyun <marethyun@uphoria.org>
*/
class StackItConnection
{
	private $user;
	private $pass;
	private $host;
	private $port;

	public $token;
	private $expireAt;

	private $stream;
	
	/**
	* 
	*/	
	function __construct($user, $pass, $host, $port)
	{
		$this->user = $user;
		$this->pass = $pass;
		$this->host = $host;
		$this->port = $port;
		$this->stream = stream_context_create(
    		array(
        		'http' => array(
        	    	'ignore_errors' => true
        		)
    		)
		);
	}

	private function checkJson($json)
	{
		if ($json['query'] != null){
			if ($json['query']['status'] == "success"){
				return true;
			} else {
				throw new Exception($json['query']['status']);
			}
		} else {
			throw new Exception("An error occured", 1);
		}
	}

	private function getJson($context)
	{	
		$json = json_decode(file_get_contents('http://'.$this->host.':'.$this->port.'/'.$context, false, $this->stream), true);
		if ($this->checkJson($json)){
			return $json;
		}
	}

	public function generateToken()
	{
		$json = $this->getJson('connect?user='.$this->user.'&pass='.$this->pass);

		$token = $json['query']['content']['token'];
		$expireAt = $json['query']['content']['expire'] + 0;
		
		$this->token = $token;
		$this->expireAt = $expireAt;
	}

	public function getPlayers($token)
	{
		$json = $this->getJson('players?token='.$token);

		return $json['query']['content']['players'];
	}

	public function getBanlist($token)
	{
		$json = $this->getJson('banlist?token='.$token);

		return $json['query']['content']['banned'];
	}

	public function getWhitelist($token)
	{
		$json = $this->getJson('whitelist?token='.$token);

		return $json['query']['content']['players'];
	}

	public function isWhitelistActivated($token)
	{
		$json = $this->getJson('whitelist?token='.$token);

		return (bool) $json['query']['content']['activated'];
	}

	public function getServerMotd($token)
	{
		$json = $this->getJson('gpi?token='.$token);

		return $json['query']['content']['motd'];
	}

	public function getPlayersMax($token)
	{
		$json = $this->getJson('gpi?token='.$token);

		return $json['query']['content']['maxplayers'];
	}

	public function getServerVersion($token)
	{
		$json = $this->getJson('gpi?token='.$token);

		return $json['query']['content']['version'];
	}

	public function update($token, $code)
	{
		$this->getJson('update?token='.$token);
	}
}