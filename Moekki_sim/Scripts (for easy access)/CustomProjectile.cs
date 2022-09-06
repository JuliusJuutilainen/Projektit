using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CustomProjectile : MonoBehaviour
{
    //Refecences
    public Rigidbody rb;
    //public GameObject explosion;
    //public LayerMask Enemy;

    //Stats
    public bool useGravity;
    public int gunDamage;
    //public float bounciness;
    //public float explosionForce;
    //public float explosionRange;

    

    //Lifetime
    //public int maxCollisions;
    public float maxLifetime;
    public bool explodeOnTouch = true;

    

    int collisions;
    PhysicMaterial physics_mat;
    void Start()
    {
        Setup();
    }

    private void Update(){
        
        //count down lifetime
        maxLifetime -= Time.deltaTime;
        if(maxLifetime <= 0) Delay();
        
    }
    

    /*private void Explode(){
        
        //Debug.Log("Explosion start");
        
        //instantiate explosion
        if(explosion != null){
            Instantiate(explosion, transform.position, Quaternion.identity);
            

        }
        //check for enemies
        Collider[] enemies = Physics.OverlapSphere(transform.position, explosionRange, Enemy);
        for (int i = 0; i < enemies.Length; i++){

            //Debug.Log("Enemy hit!");

            if(enemies[i].GetComponent<EnemyScript>()){
                enemies[i].GetComponent<EnemyScript>().TakeDamage(explosionDamage);
                Debug.Log("Enemy damaged!");
                Delay();
            }

            else{
                Invoke("Delay", 0.05f);
            }
            /*if(enemies[i].GetComponent<Rigidbody>()){
                enemies[i].GetComponent<Rigidbody>().AddExplosionForce(explosionForce, transform.position,explosionRange);
                
            }*/            
        /*}
        
        //smol delay to destroy bullet
    }*/
    

    private void Delay(){
        Destroy(gameObject);
    }

    private void OnTriggerEnter(Collider collider){
        //if(collision.collider.CompareTag("Bullet")) return;
        /*Explode();
        collisions ++;*/

        if(collider.tag == "Enemy" && explodeOnTouch){

            collider.GetComponent<EnemyScript>().TakeDamage(gunDamage);
            //Explode();
            //Debug.Log("Enemy Collided!");
        }
    }

    /*private void OnCollisionEnter(Collision collision){
        //if(collision.collider.CompareTag("Bullet")) return;
        collisions ++;

        if(collision.collider.CompareTag("Enemy") && explodeOnTouch){
            Explode();
            //Debug.Log("Enemy Collided!");
        }
        
    }*/
    private void Setup(){
        // new phys material
        /*physics_mat = new PhysicMaterial();
        physics_mat.bounciness = bounciness;
        physics_mat.frictionCombine = PhysicMaterialCombine.Minimum;
        physics_mat.frictionCombine = PhysicMaterialCombine.Maximum;

        GetComponent<SphereCollider>().material = physics_mat;*/

        //set grav
        rb.useGravity = useGravity;
    }

    /*private void OnDrawGizmosSelected(){
        Gizmos.color = Color.red;
        Gizmos.DrawWireSphere(transform.position, explosionRange);
    }*/
}
