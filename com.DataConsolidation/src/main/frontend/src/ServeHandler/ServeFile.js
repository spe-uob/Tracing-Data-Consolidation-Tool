import React from 'react';
import './ServeFile.css';
import axios from 'axios';
import { saveAs } from 'file-saver';

class ServeFile extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            file: '',
            msg: []
        }
    }
    Serve = () => {
        let data = new FormData();
        axios.get("http://localhost:8080/serve")
        .then(response =>{
            console.log(response)
            data.append('file', response.data)
            this.setState({
                file: data.get('file'),
                msg: response.headers['content-type']
            })
            console.log("data successfully received here")
            console.log(this.state.file)
        }).catch(error=>{
            this.setState({
                error : error
            });
        })
        
    }


    render(){
        return(
        <div id="container">
				<h1>Excel Files to Download</h1>
				<h4>{this.state.msg}</h4>
				<button onClick = {() => this.Serve()}>Serve</button>
                <a href = "http://localhost:8080/serve">Download</a>
	    </div>);
    }

}
export default ServeFile;