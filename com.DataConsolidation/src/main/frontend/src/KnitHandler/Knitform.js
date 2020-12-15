import React from 'react';
import styles from './Knitform.module.css';
import axios from 'axios';

class Knitform extends React.Component{
	constructor(props){
		super(props);
		this.state = {msg :''}
	}

	Knit = () => {
		this.setState({msg : 'Knit'})

		let data = new FormData();
		data.append('action', this.state.msg)
		axios.post(`http://localhost:8080/knit`, data).then(res => {
			console.log(res.data)
		}).catch(err => this.setState({error: err}));
	}

	render() {
		return (
			<div id={styles.container}>
				<button onClick={() => this.Knit()}>Knit</button>
				<a href="http://localhost:8080/serve">Download</a>
			</div>
		)
	}
}

export default Knitform;
